# AI Coach Chat MVP - Tài Liệu Demo Chi Tiết (Tiếng Việt)

Tài liệu này dùng để demo trực tiếp các tính năng AI trong app, theo trình tự rõ ràng để người nghe hiểu được:
- Tính năng giúp gì cho người học
- Hệ thống chạy như thế nào
- Kết quả mong đợi khi thao tác thực tế

---

## 1) Mục tiêu buổi demo

Sau buổi demo, người xem cần thấy rõ 6 điểm chính:
1. AI chat theo **scenario** (không chat lan man).
2. AI có **Goal/Mode** để định hướng buổi học.
3. AI có **personalization**: lồng ghép từ từ flashcard của user.
4. AI có **learning memory**: nhớ lỗi gần đây để coaching tốt hơn.
5. AI trả **feedback nhiều tầng** (quick tip + correction detail).
6. Kết thúc phiên có **session summary + next topic suggestions**.

---

## 2) Chuẩn bị trước demo (bắt buộc)

## 2.1 Chuẩn bị dữ liệu
- User demo phải có:
  - Nhiều deck flashcard
  - Mỗi deck có term rõ ràng (ví dụ: `black coffee`, `coconut milk`, `boarding pass`, `late fee`)
- Có dữ liệu scenario (cafe, travel, work, interview...)
- Có dữ liệu lịch sử AI cũ để learning memory có thể hiển thị tác dụng

## 2.2 Chuẩn bị hệ thống
- Backend chạy ổn định, kết nối DB đúng.
- Mobile app build thành công, đăng nhập được user demo.
- Kiểm tra key AI provider đang cấu hình qua env, không hard-code trong file public.

## 2.3 Checklist nhanh 2 phút trước khi lên demo
- Mở app vào sẵn màn chọn scenario.
- Xác nhận internet ổn định.
- Chuẩn bị sẵn 3-4 câu tiếng Anh cố tình sai để test correction.

---

## 3) Kịch bản demo chuẩn (15-20 phút)

## Phần A - Vào chat theo Scenario (3 phút)
**Mục tiêu:** chứng minh AI bám ngữ cảnh.

### Bước thao tác
1. Vào AI Coach.
2. Chọn scenario: `Ordering coffee`.
3. Chọn goal/mode (ví dụ Grammar hoặc Confidence).
4. Bấm bắt đầu session.

### Kỳ vọng hiển thị
- AI có opening line đúng ngữ cảnh quán cafe.
- Không nói lan sang chủ đề khác.

### Câu nói demo gợi ý
> "Ở đây AI không trả lời chung chung, mà bị khóa vào bối cảnh user đã chọn."

---

## Phần B - Gửi 3-4 lượt chat để thấy personalization (4 phút)
**Mục tiêu:** chứng minh AI lồng ghép từ user đã học từ flashcard.

### Bước thao tác
1. User nhắn câu đầu: `Hi, I want something strong.`
2. User nhắn câu thứ hai: `Can I have black coffee with coconut milk?`
3. User nhắn thêm 1-2 câu.

### Kỳ vọng hiển thị
- Trong reply AI xuất hiện từ từ flashcard.
- Từ được nhấn mạnh bằng bold (`<b>...</b>` trên UI).
- Câu trả lời vẫn tự nhiên, không liệt kê từ máy móc.

### Câu nói demo gợi ý
> "Điểm quan trọng là AI không chỉ biết từ vựng, mà dùng lại đúng lúc để tăng khả năng ghi nhớ."

---

## Phần C - Cố tình sai để xem correction + learning memory (4 phút)
**Mục tiêu:** chứng minh AI sửa lỗi và nhớ lỗi.

### Bước thao tác
1. Nhắn câu sai: `I go to coffee shop yesterday.`
2. Nhắn câu sai tiếp: `He don't like sugar.`
3. Nhắn câu đúng hơn để so sánh.

### Kỳ vọng hiển thị
- Có quick tip ngắn, không cắt mạch hội thoại.
- Có correction detail: câu sai, câu sửa, giải thích.
- Giải thích hiển thị tiếng Việt dễ hiểu.
- Các lượt sau AI ưu tiên nhắc đúng nhóm lỗi user vừa mắc.

### Câu nói demo gợi ý
> "AI ở đây không chỉ chấm lỗi theo từng câu, mà có memory để theo dõi điểm yếu lặp lại."

---

## Phần D - End session và xem Summary + Next Topic (3 phút)
**Mục tiêu:** chứng minh có tổng kết và gợi ý học tiếp.

### Bước thao tác
1. Bấm kết thúc session.
2. Mở popup/màn hình summary.
3. Bấm 1 gợi ý next topic.

### Kỳ vọng hiển thị
- Có mức độ fluency/grammar/vocabulary.
- Có số câu, số lỗi, thời lượng.
- Có danh sách next suggestions.
- Bấm next topic mở được session mới.

### Câu nói demo gợi ý
> "Sau mỗi phiên, user không bị rơi trạng thái mơ hồ mà có ngay hướng luyện tiếp."

---

## Phần E - Liên kết sang Profile (2-3 phút)
**Mục tiêu:** chứng minh dữ liệu AI cập nhật ra profile.

### Bước thao tác
1. Qua màn Profile.
2. Mở My Corrections.
3. Mở Words learned / Vocabulary list.

### Kỳ vọng hiển thị
- Time hiển thị đúng theo thời điểm kết thúc phiên (giây/phút trước).
- Vocabulary list lấy từ flashcards theo user.
- Words learned count đồng bộ với số từ thực tế trong deck/flashcard.

---

## 4) Script nói khi demo (ngắn gọn, tự tin)

## Mở đầu 30 giây
> "Nhóm em chuyển từ chatbot trả lời đơn lẻ sang AI Coach Chat theo phiên học. Mỗi phiên có mục tiêu, có memory từ lịch sử, có feedback nhiều tầng và có lộ trình học tiếp."

## Khi demo personalization
> "Các từ user đã học trong flashcard sẽ được AI lồng ghép lại đúng ngữ cảnh. Mục đích là giúp user gặp lại từ nhiều lần để nhớ lâu hơn."

## Khi demo feedback
> "Feedback có 2 tầng: tầng nhanh để giữ flow nói chuyện, và tầng chi tiết để học sâu ngay sau đó."

## Khi demo summary
> "Kết phiên không chỉ có điểm mà còn có next topic cụ thể, giúp duy trì chuỗi luyện tập."

---

## 5) Các rủi ro thường gặp và cách xử lý tại chỗ

## Rủi ro 1: AI trả lời chậm
- Cách xử lý: thông báo "Do phụ thuộc model cloud, phản hồi có thể dao động 2-5 giây".

## Rủi ro 2: Không thấy từ flashcard được lồng ghép
- Kiểm tra nhanh:
  - User có flashcards thật chưa
  - Đang dùng session mới hay session cũ
  - Scenario có phù hợp để chèn từ đó không

## Rủi ro 3: Time hiển thị chưa đúng kỳ vọng
- Kiểm tra session đã end chưa.
- Xác nhận backend trả `occurredAtEpochMs`.

## Rủi ro 4: Next topic bấm không điều hướng
- Kiểm tra payload suggestion có topic hợp lệ.
- Kiểm tra event click của item suggestion trên mobile.

---

## 6) Tiêu chí đánh giá demo thành công

Demo được coi là thành công khi đạt đủ:
- [ ] Tạo được session theo scenario.
- [ ] AI opening line đúng ngữ cảnh.
- [ ] Có ít nhất 1 từ flashcard được lồng ghép tự nhiên và hiển thị bold.
- [ ] Có quick tip + correction detail sau khi user nhắn câu sai.
- [ ] End session có summary + next suggestions.
- [ ] Bấm next topic mở session mới.
- [ ] Profile hiển thị corrections time và words learned hợp lý.

---

## 7) Phụ lục: bộ câu test gợi ý

## Câu đúng cơ bản
- `I'd like a black coffee, please.`
- `Could you make it less sweet?`

## Câu cố tình sai để test correction
- `I go to office yesterday.`
- `He don't likes this drink.`
- `I am agree with this menu.`

## Câu để test flow confidence
- `I'm a bit nervous to order in English.`
- `Can you help me say this more naturally?`

---

**Gợi ý cuối:** Trong lúc demo, luôn nói theo cấu trúc:
1) "Tính năng này giúp gì cho người học"
2) "Giờ em thao tác để chứng minh"
3) "Kết quả đang thấy trên màn hình là gì"

