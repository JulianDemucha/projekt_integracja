// src/components/RepliesList.jsx
import React, { useEffect, useState } from 'react';
import { fetchReplies } from '../api/comments';
import axios from 'axios';

const RepliesList = ({ parentId, user }) => {
    const [repliesPage, setRepliesPage] = useState(null);
    const [currentPage, setCurrentPage] = useState(0);
    const [loading, setLoading] = useState(false);
    const [deletingId, setDeletingId] = useState(null); // ID odpowiedzi w trakcie usuwania

    const loadReplies = async (page) => {
        setLoading(true);
        try {
            const data = await fetchReplies(parentId, page);
            setRepliesPage(data);
            setCurrentPage(data.number);
        } finally {
            setLoading(false);
            setDeletingId(null);
        }
    };

    useEffect(() => {
        loadReplies(0);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [parentId]);

    const handleDeleteReply = async (replyId) => {
        if (!window.confirm('Na pewno chcesz usunąć tę odpowiedź?')) return;
        setDeletingId(replyId);
        try {
            await axios.delete(`http://localhost:8080/api/comments/${replyId}`);
            loadReplies(currentPage);
        } catch (err) {
            console.error('Błąd podczas usuwania odpowiedzi:', err);
            alert('Nie udało się usunąć odpowiedzi. Sprawdź konsolę.');
            setDeletingId(null);
        }
    };

    return (
        <div>
            {loading && (
                <p className="text-sm text-center">Ładowanie odpowiedzi…</p>
            )}

            {!loading && repliesPage && (
                <>
                    {repliesPage.content.map((reply) => {
                        const authorName = reply.author?.username || 'Anonim';
                        const canDeleteReply =
                            user?.role === 'ROLE_ADMIN' || user?.id === reply.author?.id;

                        return (
                            <div key={reply.id} className="reply-item">
                                <div className="reply-header">
                                    <p className="reply-author">{authorName}</p>
                                    <div className="reply-meta">
                                        <p className="reply-date">
                                            {new Date(reply.createdAt).toLocaleString('pl-PL')}
                                        </p>
                                        {canDeleteReply && (
                                            <button
                                                onClick={() => handleDeleteReply(reply.id)}
                                                disabled={deletingId === reply.id}
                                                className="reply-delete-btn"
                                                title="Usuń odpowiedź"
                                                type="button"
                                            >
                                                ×
                                            </button>
                                        )}
                                    </div>
                                </div>
                                <p className="reply-content">{reply.content}</p>
                            </div>
                        );
                    })}

                    {/* Paginacja odpowiedzi */}
                    <div className="pagination">
                        <button
                            onClick={() => currentPage > 0 && loadReplies(currentPage - 1)}
                            disabled={currentPage === 0}
                        >
                            Poprzednie
                        </button>
                        <span className="page-info">
                            Strona {currentPage + 1} z {repliesPage.totalPages}
                        </span>
                        <button
                            onClick={() =>
                                repliesPage &&
                                currentPage < repliesPage.totalPages - 1 &&
                                loadReplies(currentPage + 1)
                            }
                            disabled={
                                repliesPage ? currentPage === repliesPage.totalPages - 1 : true
                            }
                        >
                            Następne
                        </button>
                    </div>
                </>
            )}
        </div>
    );
};

export default RepliesList;
