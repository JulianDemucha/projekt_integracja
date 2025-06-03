// src/api/comments.js
import axios from 'axios';

const API_BASE = 'http://localhost:8080';

// Liczba root-komentarzy na stronę: 10
const ROOT_PAGE_SIZE = 10;
// Liczba odpowiedzi na stronę: 5
const REPLIES_PAGE_SIZE = 5;

export const fetchRootComments = async (page = 0) => {
    const url = `${API_BASE}/api/comments/roots`;
    try {
        const response = await axios.get(url, {
            params: { page, size: ROOT_PAGE_SIZE },
        });
        return response.data; // { content: [...], totalPages, number, ... }
    } catch (error) {
        console.error('fetchRootComments – błąd HTTP:', error.response || error);
        return { content: [], totalPages: 0, number: 0 };
    }
};

export const fetchReplies = async (parentId, page = 0) => {
    const url = `${API_BASE}/api/comments/${parentId}/replies`;
    try {
        const response = await axios.get(url, {
            params: { page, size: REPLIES_PAGE_SIZE },
        });
        return response.data;
    } catch (error) {
        console.error('fetchReplies – błąd HTTP:', error.response || error);
        return { content: [], totalPages: 0, number: 0 };
    }
};

export const postRootComment = async (userId, content) => {
    const url = `${API_BASE}/api/comments/add`;
    try {
        const response = await axios.post(url, { userId, content });
        return response.data;
    } catch (error) {
        console.error('postRootComment – błąd HTTP:', error.response || error);
        throw error;
    }
};

export const postReply = async (parentId, userId, content) => {
    const url = `${API_BASE}/api/comments/reply/${parentId}`;
    try {
        const response = await axios.post(url, { userId, content });
        return response.data;
    } catch (error) {
        console.error('postReply – błąd HTTP:', error.response || error);
        throw error;
    }
};

export const deleteCommentById = async (commentId) => {
    const url = `${API_BASE}/api/comments/${commentId}`;
    try {
        await axios.delete(url);
    } catch (error) {
        console.error('deleteCommentById – błąd HTTP:', error.response || error);
        throw error;
    }
};
