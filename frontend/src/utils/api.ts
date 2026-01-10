import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8081/api/v1',
    headers: {
        'Content-Type': 'application/json'
    },
});

// Interceptor: Attach token to every request
api.interceptors.request.use(async (config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export default api;