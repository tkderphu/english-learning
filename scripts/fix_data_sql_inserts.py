"""Fix data.sql INSERT column order to match JPA BaseEntity + entity fields (idempotent)."""
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
DATA = ROOT / "src" / "main" / "resources" / "data.sql"


def split_sql_tuples(body: str) -> list[str]:
    tuples = []
    start = None
    depth = 0
    in_str = False
    esc = False
    for i, ch in enumerate(body):
        if esc:
            esc = False
            continue
        if in_str:
            if ch == "\\" and i + 1 < len(body):
                esc = True
            elif ch == "'":
                in_str = False
            continue
        if ch == "'":
            in_str = True
            continue
        if ch == "(":
            if depth == 0:
                start = i
            depth += 1
        elif ch == ")":
            depth -= 1
            if depth == 0 and start is not None:
                tuples.append(body[start : i + 1])
                start = None
    return tuples


def parse_tuple_fields(tup: str) -> list[str]:
    inner = tup[1:-1]
    parts = []
    cur = []
    depth = 0
    in_str = False
    esc = False
    for ch in inner:
        if esc:
            cur.append(ch)
            esc = False
            continue
        if in_str:
            cur.append(ch)
            if ch == "\\":
                esc = True
            elif ch == "'":
                in_str = False
            continue
        if ch == "'":
            in_str = True
            cur.append(ch)
            continue
        if ch == "(":
            depth += 1
            cur.append(ch)
            continue
        if ch == ")":
            depth -= 1
            cur.append(ch)
            continue
        if ch == "," and depth == 0:
            parts.append("".join(cur).strip())
            cur = []
            continue
        cur.append(ch)
    if cur:
        parts.append("".join(cur).strip())
    return parts


def _is_country_code(s: str) -> bool:
    return bool(re.match(r"^'[A-Z]{2}'$", s))


def fix_author_line(line: str) -> str:
    prefix = "INSERT INTO `author` VALUES "
    assert line.startswith(prefix)
    body = line[len(prefix) :].rstrip().rstrip(";")
    out = []
    for tup in split_sql_tuples(body):
        p = parse_tuple_fields(tup)
        if len(p) != 10:
            raise ValueError(f"author tuple expected 10 fields, got {len(p)}")
        id_, a, b, mb, ma, st, x1, x2, x3, x4 = p
        if _is_country_code(x1) and "http" in x4:
            created_user, created_at = a, b
            name, avatar, nationality, biography = x2, x4, x1, x3
        elif a == "'system'" and b.startswith("'20"):
            created_user, created_at = a, b
            name, avatar, nationality, biography = x1, x2, x3, x4
        elif b == "'system'" and a.startswith("'20"):
            created_user, created_at = b, a
            name, avatar, nationality, biography = x3, x1, x4, x2
        else:
            created_user, created_at = a, b
            name, avatar, nationality, biography = x3, x1, x4, x2
        out.append(
            "("
            + ",".join([id_, created_user, created_at, mb, ma, st, name, avatar, nationality, biography])
            + ")"
        )
    return prefix + ",".join(out) + ";"


def fix_book_line(line: str) -> str:
    prefix = "INSERT INTO `book` VALUES "
    body = line[len(prefix) :].rstrip().rstrip(";")
    out = []
    for tup in split_sql_tuples(body):
        p = parse_tuple_fields(tup)
        if len(p) != 9:
            raise ValueError(f"book tuple expected 9 fields, got {len(p)}")
        id_, a, b, c, d, st, x1, x2, x3 = p
        if a.startswith("'20") and (b == "'system'" or b.startswith("'User(")):
            cu, ca = b, a
            mb, ma = d, c
        elif a == "'system'" and b.startswith("'20"):
            cu, ca = a, b
            mb, ma = c, d
        elif a.startswith("'User(") and b.startswith("'20"):
            cu, ca = a, b
            mb, ma = c, d
        else:
            cu, ca = a, b
            mb, ma = c, d
        if "http" in x1 and x1.startswith("'http") and not x3.startswith("'http"):
            cover, lang, title = x1, x2, x3
        else:
            title, lang, cover = x1, x2, x3
        out.append("(" + ",".join([id_, cu, ca, mb, ma, st, title, lang, cover]) + ")")
    return prefix + ",".join(out) + ";"


def fix_genre_line(line: str) -> str:
    prefix = "INSERT INTO `genre` VALUES "
    body = line[len(prefix) :].rstrip().rstrip(";")
    out = []
    for tup in split_sql_tuples(body):
        p = parse_tuple_fields(tup)
        if len(p) != 9:
            raise ValueError(f"genre tuple expected 9 fields, got {len(p)}")
        id_, a, b, c, d, st, x1, x2, x3 = p
        if x1.startswith("'http"):
            thumb, desc_, name = x1, x2, x3
        else:
            name, thumb, desc_ = x1, x2, x3
        if a == "'system'" and b.startswith("'20"):
            cu, ca, mb, ma = a, b, c, d
        elif b == "'system'" and a.startswith("'20"):
            cu, ca = b, a
            mb, ma = d, c
        else:
            cu, ca = a, b
            mb, ma = c, d
        out.append("(" + ",".join([id_, cu, ca, mb, ma, st, name, thumb, desc_]) + ")")
    return prefix + ",".join(out) + ";"


def fix_user_line(line: str) -> str:
    prefix = "INSERT INTO `user` VALUES "
    body = line[len(prefix) :].rstrip().rstrip(";")
    tups = split_sql_tuples(body)
    if len(tups) != 1:
        raise ValueError("expected single user tuple")
    p = parse_tuple_fields(tups[0])
    if len(p) != 11:
        raise ValueError(f"user tuple expected 11 fields, got {len(p)}")
    id_, a, b, c, d, st, p6, p7, p8, p9, p10 = p
    if a.startswith("'20") and b.startswith("'User("):
        cu, ca, mb, ma = b, a, d, c
    else:
        cu, ca, mb, ma = a, b, c, d
    # JPA order output: email, password, full_name, avatar, role (idempotent)
    if p6.startswith("'$2a$"):
        em, pw, fn, av, role = p9, p6, p8, p7, p10
    elif "@" in p6:
        em, pw, fn, av, role = p6, p7, p8, p9, p10
    else:
        em, pw, fn, av, role = p7, p9, p8, p6, p10
    out = "(" + ",".join([id_, cu, ca, mb, ma, st, em, pw, fn, av, role]) + ")"
    return prefix + out + ";"


def fix_book_progress_line(line: str) -> str:
    prefix = "INSERT INTO `book_progress` VALUES "
    body = line[len(prefix) :].rstrip().rstrip(";")
    out = []
    for idx, tup in enumerate(split_sql_tuples(body)):
        p = parse_tuple_fields(tup)
        if len(p) == 13:
            p = p[:12]
        if idx == 1 and len(p) == 12:
            id_ = p[0]
            out.append(
                "("
                + ",".join(
                    [
                        id_,
                        "NULL",
                        "NULL",
                        "NULL",
                        "NULL",
                        p[5],
                        "1",
                        "125",
                        "0",
                        "NULL",
                        "NULL",
                        "NULL",
                    ]
                )
                + ")"
            )
            continue
        if len(p) != 12:
            raise ValueError(f"book_progress tuple expected 12 fields, got {len(p)}")
        id_, a, b, c, d, st, u, bk, pp, lrt, fav, lpn = p
        if (
            a is not None
            and a != "NULL"
            and a.startswith("'20")
            and b is not None
            and b.startswith("'User(")
        ):
            cu, ca, mb, ma = b, a, d, c
        elif a == "NULL":
            cu, ca, mb, ma = a, b, c, d
        else:
            cu, ca, mb, ma = a, b, c, d
        if u == "16" and bk == "1":
            u, bk = "1", "16"
        if pp == "NULL" and lrt == "10":
            pp, lrt = "10", "NULL"
        out.append("(" + ",".join([id_, cu, ca, mb, ma, st, u, bk, pp, lrt, fav, lpn]) + ")")
    return prefix + ",".join(out) + ";"


def main():
    text = DATA.read_text(encoding="utf-8")
    lines = text.splitlines()
    new_lines = []
    for line in lines:
        if line.startswith("INSERT INTO `author`"):
            new_lines.append(fix_author_line(line))
        elif line.startswith("INSERT INTO `book`"):
            new_lines.append(fix_book_line(line))
        elif line.startswith("INSERT INTO `genre`"):
            new_lines.append(fix_genre_line(line))
        elif line.startswith("INSERT INTO `user`"):
            new_lines.append(fix_user_line(line))
        elif line.startswith("INSERT INTO `book_progress`"):
            new_lines.append(fix_book_progress_line(line))
        else:
            new_lines.append(line)
    DATA.write_text("\n".join(new_lines) + "\n", encoding="utf-8")
    print("Wrote", DATA)


if __name__ == "__main__":
    main()
