# Kịch Bản Demo AI Coach Chat (Giải Thích Kỹ Cho Thầy)

Tài liệu này là script thực chiến để demo trước giảng viên. Mục tiêu là:
- Không chỉ "bấm chạy tính năng", mà giải thích rõ **vì sao tính năng có giá trị học tập**
- Trình bày được **luồng kỹ thuật** ở mức đủ sâu cho đồ án
- Có sẵn phương án xử lý khi demo gặp sự cố

---

## 1) Bức tranh tổng thể cần nói trước khi demo (45-60 giây)

### 1.1 Câu mở đầu gợi ý
> "Sản phẩm của nhóm em là AI Coach Chat cho người học tiếng Anh theo phiên luyện tập. Mỗi phiên có mục tiêu, có phản hồi theo nhiều tầng, có cơ chế ghi nhớ lỗi gần đây, và có gợi ý học tiếp sau khi kết thúc phiên."

### 1.2 4 ý cốt lõi thầy cần nghe
1. **Có định hướng học**: vào chat là theo scenario/goal/mode, không lan man.
2. **Có cá nhân hóa**: AI ưu tiên lồng từ user đã học trong flashcard.
3. **Có learning memory**: lỗi lặp lại được nhắc có chủ đích, không sửa rời rạc.
4. **Có vòng học khép kín**: chat -> sửa lỗi -> tổng kết -> gợi ý phiên tiếp theo.

---

## 2) Chuẩn bị trước demo (bắt buộc)

## 2.1 Dữ liệu test
- User demo cần có ít nhất 2-3 deck flashcard.
- Trong deck nên có cả từ đơn và cụm từ (vd: `black coffee`, `boarding pass`, `late fee`).
- Có sẵn scenario phổ biến: cafe, travel, office, interview.

## 2.2 Môi trường
- Backend chạy ổn định, kết nối đúng DB demo.
- FE trỏ đúng `BASE_URL` backend demo.
- Tài khoản demo đăng nhập sẵn.

## 2.3 Checklist 1 phút trước khi vào lớp
- Mở sẵn màn chọn topic AI.
- Kiểm tra internet.
- Chuẩn bị sẵn 3 câu sai ngữ pháp để test sửa lỗi.
- Chuẩn bị sẵn 1 câu để test "lặp lỗi" (learning memory).

---

## 3) Kịch bản demo theo timeline (15-20 phút)

## 3.0 Demo điểm khác biệt Goal và Mode (bắt buộc, 5-7 phút)

Mục tiêu của phần này là để thầy thấy rõ đây không phải "đổi tên nút", mà là thay đổi cách AI coaching.

### 3.0.1 Cách demo rõ nhất (A/B test cùng một câu)

Dùng cùng một ngữ cảnh (ví dụ cafe) và cùng một câu đầu vào:
- `I go cafe yesterday and speak with barista.`

Chạy lặp lại 3 lần với 3 Goal, rồi 2 lần với 2 Mode để so sánh.

---

### 3.0.2 So sánh 3 Goal: COMMUNICATION vs FLUENCY vs GRAMMAR

## A) Goal = COMMUNICATION
**Bạn nói với thầy:**
> "Mục tiêu này ưu tiên truyền đạt ý, giữ hội thoại tự nhiên, AI vẫn sửa lỗi nhưng không làm người học bị ngắt mạch quá mạnh."

**Kỳ vọng quan sát:**
- AI phản hồi thân thiện, khuyến khích tiếp tục nói.
- Quick tip thiên về "nói sao để giao tiếp trôi chảy trong ngữ cảnh".
- Correction có, nhưng mức độ "soi ngữ pháp chi li" thấp hơn GRAMMAR.

## B) Goal = FLUENCY
**Bạn nói với thầy:**
> "Mục tiêu này ưu tiên tốc độ và độ trôi khi diễn đạt. AI sẽ tập trung vào cách nối câu, nhịp câu, cụm diễn đạt mượt hơn."

**Kỳ vọng quan sát:**
- AI đưa gợi ý diễn đạt mượt hơn thay vì chỉ sửa đúng/sai ngữ pháp.
- Khuyến nghị các cấu trúc giúp nói liền mạch hơn (linking phrases, tự nhiên hơn).
- Ít nhấn mạnh chi tiết lỗi ngữ pháp đơn lẻ hơn GRAMMAR.

## C) Goal = GRAMMAR
**Bạn nói với thầy:**
> "Mục tiêu này ưu tiên độ chính xác ngữ pháp. AI sẽ chỉ ra lỗi thì, chia động từ, cấu trúc câu rõ hơn hai goal còn lại."

**Kỳ vọng quan sát:**
- Correction cụ thể hơn, chỉ rõ lỗi và câu đúng.
- Giải thích nghiêng về quy tắc ngữ pháp.
- Tông phản hồi "coaching kỹ lỗi" rõ hơn COMMUNICATION.

---

### 3.0.3 So sánh 2 Mode: COACH vs FLUENCY

Để công bằng, giữ nguyên Goal (ví dụ COMMUNICATION), chỉ đổi Mode.

## A) Mode = COACH
**Bạn nói với thầy:**
> "COACH là mode cân bằng: vừa động viên, vừa chỉnh lỗi theo mức vừa đủ để người học tiến bộ ổn định."

**Kỳ vọng:**
- Có nhắc lỗi + hướng sửa.
- Có khích lệ và định hướng học tiếp.
- Không ép tập trung quá mạnh vào nhịp nói.

## B) Mode = FLUENCY
**Bạn nói với thầy:**
> "FLUENCY mode tập trung mạnh vào dòng nói liên tục, cách diễn đạt mượt và tự nhiên. Tức là ưu tiên 'nói trôi' trước, rồi mới tinh chỉnh sâu."

**Kỳ vọng:**
- AI ưu tiên câu trả lời giúp người học nói tiếp nhanh.
- Gợi ý diễn đạt tự nhiên/flow nhiều hơn.
- Trải nghiệm cảm giác "đang luyện nói thực tế", ít bị khựng vì quá nhiều lỗi nhỏ.

---

### 3.0.4 Câu chốt phần so sánh (nên nói nguyên văn)
> "Như thầy thấy, Goal quyết định *học cái gì là chính* (giao tiếp, độ trôi, hay ngữ pháp), còn Mode quyết định *cách AI kèm cặp* trong phiên đó. Hai lớp này kết hợp để cá nhân hóa buổi học thay vì một kiểu phản hồi cố định."

### 3.0.5 Giao diện: thanh gợi ý dưới cùng và cách test để thấy khác biệt thật

- **Thanh gợi ý (dưới ô nhập)** chỉ hiển thị nội dung gợi ý ngắn (ưu tiên `naturalSuggestion`), **không** lặp lại khối “thiết lập phiên” dài. Mục tiêu / chế độ đã có trên dòng tiêu đề (ví dụ `BEGINNER • GRAMMAR • COACH`).
- **Để thấy Goal khác nhau**, đừng chỉ nhìn một chỗ: so sánh **(1)** câu tiếng Anh của AI (nhân vật), **(2)** thẻ “Sửa lỗi nhanh” + giải thích, **(3)** thanh gợi ý dưới cùng.
- **Bắt buộc tạo phiên mới** sau khi đổi Goal (mỗi session gắn một cấu hình).

**Cùng một câu test (nên dùng):** `I go cafe yesterday and speak with barista.`

| Goal | Tín hiệu dễ thấy |
|------|------------------|
| **GRAMMAR** | Thẻ sửa lỗi nhấn mạnh thì/cấu trúc; `overallComment` (trong luồng feedback) nêu rõ lỗi ngữ pháp chính; AI trong hội thoại thường lồng một câu mẫu đúng ngữ pháp. |
| **FLUENCY** | AI trả lời rất ngắn, giữ nhịp; ít mục lỗi trong `errors[]`; gợi ý thiên về cách nói trôi, có thể kèm một câu tiếng Anh ngắn để nói lại. |
| **COMMUNICATION** | AI mở đầu thân thiện, đẩy tình huống (gọi món, hỏi ý); feedback ưu tiên “hiểu ý bạn”, tối đa ~1 lỗi trong `errors[]` trừ khi sai nặng làm mất nghĩa. |

## Phần A - Tạo phiên AI theo ngữ cảnh (3 phút)

### Mục tiêu sư phạm
Chứng minh AI không phải chatbot trả lời chung chung, mà là "coach theo bài học".

### Thao tác
1. Vào tab chat AI.
2. Chọn 1 scenario (vd: gọi đồ uống).
3. Chọn Goal/Mode rồi bắt đầu.

### Cần giải thích cho thầy
- Scenario quyết định "bối cảnh luyện tập".
- Goal/Mode giúp AI ưu tiên kiểu feedback (grammar/fluency/coaching).
- Mỗi lần bắt đầu là tạo session mới để theo dõi tiến trình.

### Kết quả kỳ vọng trên UI
- AI mở đầu đúng ngữ cảnh đã chọn.
- Có cảm giác "đi vào bài học" ngay, không mất thời gian thiết lập thủ công.

---

## Phần B - Demo cá nhân hóa từ flashcard (3-4 phút)

### Mục tiêu sư phạm
Chứng minh AI hỗ trợ "ôn lặp lại có ngữ cảnh", giúp nhớ từ lâu hơn.

### Thao tác
1. User gửi 2-3 câu trong đoạn hội thoại.
2. Quan sát reply AI có dùng lại từ từ flashcard.

### Cần giải thích cho thầy
- Hệ thống lấy danh sách từ ưu tiên từ kho flashcard của user.
- AI được hướng dẫn dùng từ đó trong câu trả lời tự nhiên.
- Khi lồng được từ quan trọng, UI hiển thị in đậm để người học chú ý.

### Kết quả kỳ vọng
- Có ít nhất 1 cụm từ đã học xuất hiện trong phản hồi AI.
- Không phải chèn từ kiểu liệt kê; vẫn đúng ngữ cảnh hội thoại.

---

## Phần C - Demo correction nhiều tầng + learning memory (4-5 phút)

### Mục tiêu sư phạm
Chứng minh hệ thống sửa lỗi theo hướng "dạy học", không chỉ báo đúng/sai.

### Thao tác gợi ý
1. Gửi câu sai 1: `I go to coffee shop yesterday.`
2. Gửi câu sai 2: `He don't like sugar.`
3. Lặp lại một lỗi tương tự để kích hoạt nhắc lỗi lặp.

### Cần giải thích cho thầy
- **Tầng 1 (quick tip)**: gợi ý ngắn, không làm đứt mạch chat.
- **Tầng 2 (chi tiết)**: chỉ rõ câu sai, câu đề xuất đúng, giải thích.
- Với lỗi lặp, hệ thống thêm nhắc nhở "đây là lỗi đang lặp lại" để tăng nhận thức.

### Kết quả kỳ vọng
- UI hiện khối sửa lỗi rõ ràng (sai/đúng/giải thích).
- Giải thích hiển thị tiếng Việt dễ hiểu.
- Lượt sau có dấu hiệu nhấn mạnh lỗi người học đang lặp lại.

---

## Phần D - Kết thúc phiên và dùng Next Topic (3-4 phút)

### Mục tiêu sư phạm
Chứng minh học theo chu trình liên tục, không bị "đứt phiên".

### Thao tác
1. Bấm kết thúc session.
2. Mở popup summary.
3. Bấm một chủ đề trong "Gợi ý chủ đề tiếp theo".

### Cần giải thích cho thầy
- Summary tổng hợp nhanh: thời lượng, số câu, số lỗi, mức độ thành phần.
- Next topic biến thành hành động ngay: tạo session mới từ gợi ý.
- Đây là cách giữ "đà học", tránh user rời app sau mỗi phiên.

### Kết quả kỳ vọng
- Bấm gợi ý là chuyển sang phiên chat mới được ngay.

---

## Phần E - Chốt vòng đời dữ liệu học tập (2 phút)

### Mục tiêu sư phạm
Cho thấy dữ liệu AI không mất đi sau phiên chat.

### Cần trình bày
- Lỗi và từ vựng liên quan được đồng bộ về khu vực profile tương ứng.
- Words learned phản ánh nguồn từ kho flashcard người dùng.
- Logic backend và UI phải nhất quán theo user hiện tại.

---

## 4) Giải thích kiến trúc ngắn gọn (khi thầy hỏi "hệ thống chạy sao?")

## 4.1 Luồng runtime
1. FE gửi request tạo session.
2. BE lưu session + chọn context (scenario/goal/mode).
3. Mỗi user message:
   - BE gọi model tạo feedback và AI reply
   - Lưu transcript + feedback + lỗi
4. Khi end session:
   - BE tạo summary
   - Trả next suggestions
   - Ghi nhận activity để phục vụ thống kê

## 4.2 Vì sao dùng nhiều lớp feedback?
- Quick tip: hỗ trợ hội thoại realtime.
- Chi tiết: hỗ trợ học sâu sau mỗi câu.
- Cách này cân bằng giữa "trải nghiệm nói chuyện" và "chất lượng học".

---

## 5) Bộ câu demo mẫu (để nói trơn tru)

## 5.1 Câu bình thường
- `I'd like a black coffee, please.`
- `Could you make it less sweet?`

## 5.2 Câu cố tình sai
- `I go to the office yesterday.`
- `He don't like this drink.`
- `I am agree with this option.`

## 5.3 Câu để test confidence/coach
- `I'm nervous when I speak English.`
- `Can you suggest a more natural way to say this?`

---

## 6) Câu trả lời mẫu cho câu hỏi khó của thầy

## "AI này khác chatbot thường ở đâu?"
> "Chatbot thường trả lời theo từng lượt độc lập. Hệ thống này có session, có mục tiêu học, có memory lỗi gần đây và có summary để định hướng phiên tiếp theo."

## "Tại sao cần lồng từ flashcard?"
> "Vì học ngôn ngữ cần gặp lại từ trong ngữ cảnh thật nhiều lần. Lồng từ giúp chuyển từ 'nhớ mặt chữ' sang 'dùng được trong giao tiếp'."

## "Nếu model trả sai thì sao?"
> "Bọn em giới hạn schema đầu ra, lưu lại phản hồi có cấu trúc, và thiết kế UI/flow để user vẫn có gợi ý học an toàn kể cả khi một phần phản hồi không hoàn hảo."

---

## 7) Kịch bản dự phòng khi demo lỗi

## Tình huống 1: API chậm hoặc timeout
- Nói trước: phản hồi AI cloud có độ trễ dao động.
- Chuyển nhanh sang phần đã có dữ liệu sẵn (summary/corrections).

## Tình huống 2: Next topic không mở phiên mới
- Kiểm tra nhanh backend đã end session thành công.
- Thử lại bằng topic khác trong list.

## Tình huống 3: Không thấy từ flashcard lồng ghép
- Đảm bảo user demo có deck/từ hợp lệ.
- Dùng câu chat bám sát context scenario để model dễ chèn từ.

---

## 8) Tiêu chí buổi demo đạt yêu cầu

- [ ] Tạo phiên theo scenario thành công.
- [ ] Thể hiện được personalization từ flashcard.
- [ ] Thấy correction rõ ràng cho câu sai.
- [ ] Thấy dấu hiệu learning memory khi lỗi lặp lại.
- [ ] End session có summary + next topics.
- [ ] Bấm next topic mở phiên mới.
- [ ] Trình bày rõ "lợi ích học tập" của từng tính năng.

---

## 9) Chốt demo (20-30 giây)

> "Điểm mạnh của AI Coach Chat không nằm ở một câu trả lời hay, mà ở cả vòng học: định hướng đúng, sửa lỗi đúng trọng tâm, nhớ điểm yếu gần đây và dẫn người học sang phiên tiếp theo. Nhờ vậy hiệu quả học được duy trì liên tục thay vì rời rạc từng lượt chat."

