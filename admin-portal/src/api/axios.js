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
