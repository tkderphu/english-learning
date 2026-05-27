/**
 * axios.js - Cấu hình Axios chung cho Admin Portal
 *
 * Khởi tạo axios instance với baseURL là `/api`.
 * Response interceptor được cấu hình để tự động bóc tách `response.data`
 * (BaseResponse) ra khỏi axios response nguyên thủy, giúp các hàm gọi API
 * nhận thẳng dữ liệu cần thiết.
 */
import axios from 'axios';

const instance = axios.create({
    baseURL: '/api',
});

// Response interceptor to handle data extraction
instance.interceptors.response.use(
    (response) => response.data, // Returns the BaseResponse body directly
    (error) => {
        console.error('API Error:', error);
        return Promise.reject(error);
    }
);

export default instance;
