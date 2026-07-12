import axios from 'axios';

export const api = axios.create({
  baseURL: 'http://localhost:8080',
  headers: { 'Content-Type': 'application/json' },
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const data = error.response?.data;
    let message = 'Erro inesperado';

    if (data?.message) {
      message = data.message;
    } else if (data?.error) {
      message = data.error;
    } else if (error.message) {
      message = error.message;
    }

    if (data?.fieldErrors?.length) {
      const fieldMessages = data.fieldErrors.map((f: { message: string }) => f.message).join('; ');
      message = `${message}: ${fieldMessages}`;
    }

    return Promise.reject(new Error(message));
  },
);
