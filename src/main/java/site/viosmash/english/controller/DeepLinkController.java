package site.viosmash.english.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import site.viosmash.english.entity.Book;
import site.viosmash.english.repository.BookRepository;

import java.util.Optional;

/**
 * Trang hạ cánh (landing) cho deep link `https://<domain>/book/{id}`.
 *
 * Luồng hoạt động:
 *  - Nếu người nhận đã cài app FluLingO và Android App Links đã verify (assetlinks.json OK),
 *    hệ điều hành sẽ tự mở thẳng app, KHÔNG bao giờ gọi đến endpoint này.
 *  - Nếu chưa cài app hoặc mở trong trình duyệt / webview không hỗ trợ App Links:
 *      → endpoint này trả về 1 trang HTML có:
 *          + Meta Open Graph để Zalo / Messenger / WhatsApp / Telegram hiển thị preview đẹp.
 *          + Nút "Mở trong FluLingO" dùng `intent://` để thử mở app, fallback sang Play Store.
 *          + Tự động redirect sang `intent://...` sau 500ms trên Android.
 *
 * Endpoint KHÔNG nằm dưới `/api` để tránh bị JWT filter chặn và để đúng path chia sẻ.
 */
@Controller
@RequiredArgsConstructor
public class DeepLinkController {

    private static final String ANDROID_PACKAGE = "com.mit.learning_english";
    private static final String DEEP_LINK_SCHEME = "flulingo";
    private static final String DEEP_LINK_HOST = "book";

    private final BookRepository bookRepository;

    @Value("${server.public.domain:}")
    private String publicDomain;

    @GetMapping(value = "/book/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> bookLandingPage(@PathVariable("id") int id) {
        Optional<Book> bookOpt = bookRepository.findById(id);

        String title = bookOpt.map(Book::getTitle).orElse("FluLingO");
        String coverUrl = bookOpt.map(Book::getCoverUrl).orElse("");
        String canonicalUrl = buildCanonicalUrl(id);
        String description = bookOpt.isPresent()
                ? "Đọc \"" + title + "\" trên FluLingO – ứng dụng học tiếng Anh qua sách."
                : "Mở sách trong FluLingO.";

        String html = renderHtml(id, title, coverUrl, canonicalUrl, description);
        return ResponseEntity.ok()
                .header("Cache-Control", "public, max-age=300")
                .body(html);
    }

    private String buildCanonicalUrl(int id) {
        String base = (publicDomain == null || publicDomain.isBlank())
                ? "https://english.kimchimar3.store"
                : publicDomain.replaceAll("/$", "");
        return base + "/book/" + id;
    }

    private String renderHtml(int id, String title, String coverUrl,
                              String canonicalUrl, String description) {
        String safeTitle = escapeHtml(title);
        String safeDescription = escapeHtml(description);
        String safeCover = escapeHtml(coverUrl);
        String safeCanonical = escapeHtml(canonicalUrl);

        // intent:// URL: thử mở app; nếu chưa cài sẽ rơi vào S.browser_fallback_url → Play Store.
        String playStoreUrl = "https://play.google.com/store/apps/details?id=" + ANDROID_PACKAGE;
        String intentUrl = "intent://" + DEEP_LINK_HOST + "/" + id
                + "#Intent;scheme=" + DEEP_LINK_SCHEME
                + ";package=" + ANDROID_PACKAGE
                + ";S.browser_fallback_url=" + urlEncode(playStoreUrl)
                + ";end";
        String customSchemeUrl = DEEP_LINK_SCHEME + "://" + DEEP_LINK_HOST + "/" + id;

        return "<!DOCTYPE html>\n"
                + "<html lang=\"vi\">\n"
                + "<head>\n"
                + "  <meta charset=\"UTF-8\">\n"
                + "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "  <title>" + safeTitle + " – FluLingO</title>\n"
                + "  <meta name=\"description\" content=\"" + safeDescription + "\">\n"
                + "  <link rel=\"canonical\" href=\"" + safeCanonical + "\">\n"
                // Open Graph
                + "  <meta property=\"og:type\" content=\"book\">\n"
                + "  <meta property=\"og:title\" content=\"" + safeTitle + "\">\n"
                + "  <meta property=\"og:description\" content=\"" + safeDescription + "\">\n"
                + "  <meta property=\"og:url\" content=\"" + safeCanonical + "\">\n"
                + (coverUrl.isBlank() ? "" : "  <meta property=\"og:image\" content=\"" + safeCover + "\">\n")
                + "  <meta property=\"og:site_name\" content=\"FluLingO\">\n"
                // Twitter card
                + "  <meta name=\"twitter:card\" content=\"summary_large_image\">\n"
                + "  <meta name=\"twitter:title\" content=\"" + safeTitle + "\">\n"
                + "  <meta name=\"twitter:description\" content=\"" + safeDescription + "\">\n"
                + (coverUrl.isBlank() ? "" : "  <meta name=\"twitter:image\" content=\"" + safeCover + "\">\n")
                // App Links hint (giúp 1 số trình duyệt tự mở app)
                + "  <meta property=\"al:android:package\" content=\"" + ANDROID_PACKAGE + "\">\n"
                + "  <meta property=\"al:android:url\" content=\"" + customSchemeUrl + "\">\n"
                + "  <meta property=\"al:android:app_name\" content=\"FluLingO\">\n"
                + "  <style>\n"
                + "    * { box-sizing: border-box; }\n"
                + "    body { margin: 0; font-family: -apple-system, BlinkMacSystemFont, \"Segoe UI\", Roboto, sans-serif;\n"
                + "           background: linear-gradient(135deg,#6366f1,#8b5cf6); min-height: 100vh; display: flex;\n"
                + "           align-items: center; justify-content: center; color: #1f2937; padding: 24px; }\n"
                + "    .card { background: #fff; border-radius: 20px; max-width: 420px; width: 100%;\n"
                + "            padding: 32px 24px; text-align: center; box-shadow: 0 20px 60px rgba(0,0,0,.25); }\n"
                + "    .logo { font-weight: 800; font-size: 22px; color: #6366f1; margin-bottom: 4px; }\n"
                + "    .cover { width: 180px; height: 260px; object-fit: cover; border-radius: 12px;\n"
                + "             margin: 16px auto; box-shadow: 0 10px 24px rgba(0,0,0,.15); display: block; }\n"
                + "    h1 { font-size: 20px; margin: 8px 0 4px; }\n"
                + "    p  { color: #6b7280; margin: 4px 0 20px; font-size: 14px; }\n"
                + "    .btn { display: block; width: 100%; padding: 14px 16px; border-radius: 12px;\n"
                + "           font-weight: 600; text-decoration: none; margin-top: 10px; font-size: 15px; }\n"
                + "    .btn-primary { background: #6366f1; color: #fff; }\n"
                + "    .btn-primary:active { background: #4f46e5; }\n"
                + "    .btn-ghost   { background: #f3f4f6; color: #374151; }\n"
                + "    small { color:#9ca3af; display:block; margin-top:18px; font-size:12px; }\n"
                + "  </style>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <div class=\"card\">\n"
                + "    <div class=\"logo\">FluLingO</div>\n"
                + (coverUrl.isBlank()
                    ? ""
                    : "    <img class=\"cover\" src=\"" + safeCover + "\" alt=\"" + safeTitle + "\" onerror=\"this.style.display='none'\">\n")
                + "    <h1>" + safeTitle + "</h1>\n"
                + "    <p>" + safeDescription + "</p>\n"
                + "    <a id=\"openAppBtn\" class=\"btn btn-primary\" href=\"" + escapeHtml(intentUrl) + "\">Mở trong FluLingO</a>\n"
                + "    <a class=\"btn btn-ghost\" href=\"" + escapeHtml(playStoreUrl) + "\">Tải app trên Google Play</a>\n"
                + "    <small>Nếu app không tự mở, hãy bấm nút phía trên.</small>\n"
                + "  </div>\n"
                + "  <script>\n"
                + "    (function () {\n"
                + "      var ua = navigator.userAgent || '';\n"
                + "      var isAndroid = /Android/i.test(ua);\n"
                + "      if (isAndroid) {\n"
                + "        // Tự động thử mở app sau khi trang load\n"
                + "        setTimeout(function () {\n"
                + "          window.location.href = " + jsString(intentUrl) + ";\n"
                + "        }, 400);\n"
                + "      }\n"
                + "    })();\n"
                + "  </script>\n"
                + "</body>\n"
                + "</html>";
    }

    private static String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private static String jsString(String s) {
        if (s == null) return "\"\"";
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    private static String urlEncode(String s) {
        return java.net.URLEncoder.encode(s, java.nio.charset.StandardCharsets.UTF_8);
    }
}
