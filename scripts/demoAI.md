Kịch Bản Demo AI Coach Chat MVP (1 tuần)
1) Mục tiêu demo
   Cho thấy AI chat đã có định hướng buổi học.
   Cho thấy AI có cá nhân hóa theo lỗi gần đây.
   Cho thấy feedback có 2 tầng rõ ràng: nhanh + chi tiết.
2) Chuẩn bị trước demo
   Đăng nhập user đã có lịch sử AI chat (để có lỗi cũ).
   Đảm bảo backend + mobile đang chạy bản mới.
   Vào tab AI Chat (ChooseTopic).
3) Flow demo chính (3-5 phút)
   Bước 1: Guided setup

Chọn Goal (ví dụ: FLUENCY).
Chọn Mode (ví dụ: COACH hoặc FLUENCY).
Chọn 1 scenario (vd. hotel check-in) hoặc Free Talk.
Kỳ vọng: Vào màn chat, header hiển thị level + goal + mode.
Bước 2: Chat 2-3 lượt

User gửi 1 câu có lỗi grammar/vocab.
Kỳ vọng: AI trả lời bình thường + hiện quick feedback ngắn (1 câu).
User gửi thêm 1 câu.
Kỳ vọng: vẫn có quick feedback liên tục, không vỡ flow hội thoại.
Bước 3: Detail feedback

Mở phần correction (card lỗi) để xem chi tiết.
Kỳ vọng: thấy lỗi gốc, câu sửa, gợi ý tự nhiên hơn (detail layer).
Bước 4: Cá nhân hóa (nói bằng lời trong demo)

Giải thích: prompt đã được inject từ lỗi gần đây + điểm yếu gần đây + goal hiện tại.
Kỳ vọng thực tế: AI dẫn dắt đúng trọng tâm hơn (ví dụ chú ý fluency/grammar theo mode).
4) Script thuyết trình ngắn (đọc trực tiếp)
   “Trước đây AI chat phản hồi chung, còn bây giờ trước khi vào chat mình chọn Goal và Mode nên buổi học có định hướng ngay.”
   “Trong lúc chat, app cho quick feedback rất ngắn để không ngắt flow nói.”
   “Nếu cần học kỹ, mình mở detail feedback để xem lỗi, câu sửa và cách nói tự nhiên hơn.”
   “Backend còn nhớ lỗi gần đây của user và bơm vào prompt, nên mỗi session có cảm giác được kèm riêng.”
5) Kết quả mong đợi để chốt demo
   Có Guided AI Chat.
   Có Personalized Coach Focus (nhớ lỗi gần đây).
   Có Smart Correction Feedback (quick + detail).
   Không phá flow cũ, không cần migration lớn, chạy ổn định cho demo tuần này.