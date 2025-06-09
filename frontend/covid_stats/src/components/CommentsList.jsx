import React, {useEffect, useState, useContext} from 'react';
import {fetchRootComments, postRootComment} from '../api/comments';
import CommentItem from './CommentItem';
import {AuthContext} from '../context/AuthContext';

const CommentsList = () => {
    const {user} = useContext(AuthContext);
    const [commentsPage, setCommentsPage] = useState({
        content: [],
        totalPages: 0,
        number: 0,
    });
    const [currentPage, setCurrentPage] = useState(0);
    const [newContent, setNewContent] = useState('');
    const [loading, setLoading] = useState(false);
    const [errorAdding, setErrorAdding] = useState(null);

    const loadComments = async (page) => {
        setLoading(true);
        try {
            const data = await fetchRootComments(page);
            setCommentsPage(data);
            setCurrentPage(data.number);
        } catch (err) {
            console.error('loadComments – nieoczekiwany błąd:', err);
            setCommentsPage({content: [], totalPages: 0, number: 0});
            setCurrentPage(0);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadComments(0);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const handlePrev = () => {
        if (currentPage > 0) loadComments(currentPage - 1);
    };

    const handleNext = () => {
        if (currentPage < commentsPage.totalPages - 1)
            loadComments(currentPage + 1);
    };

    const handleAddComment = async (e) => {
        e.preventDefault();
        setErrorAdding(null);

        if (!newContent.trim()) return;
        if (!user) {
            setErrorAdding('Musisz być zalogowany, aby dodać komentarz.');
            return;
        }

        try {
            await postRootComment(user.id, newContent.trim());
            setNewContent('');
            loadComments(0);
        } catch (err) {
            console.error('Błąd podczas dodawania komentarza:', err.response || err);
            setErrorAdding('Nie udało się dodać komentarza.');
        }
    };

    return (
        <div className="comments-container">
            <h2 className="comments-header">Komentarze</h2>

            <form onSubmit={handleAddComment} className="new-comment-form">
                <textarea
                    rows={3}
                    placeholder="Dodaj nowy komentarz..."
                    value={newContent}
                    maxLength={500}
                    onChange={(e) => setNewContent(e.target.value)}
                    disabled={!user}
                />
                <div>
                    <button
                        type="submit"
                        className="btn-submit"
                        disabled={!user || !newContent.trim()}
                        style={{marginBottom: '10px'}}
                    >
                        {user ? 'Dodaj komentarz' : 'Musisz być zalogowany, żeby dodać komentarz'}
                    </button>
                    {errorAdding && (
                        <p className="error-message">{errorAdding}</p>
                    )}
                </div>
            </form>

            {loading && <p className="text-center">Ładowanie komentarzy…</p>}

            {!loading && Array.isArray(commentsPage.content) && (
                <>
                    {commentsPage.content.length === 0 ? (
                        <p className="text-center">Brak komentarzy do wyświetlenia.</p>
                    ) : (
                        commentsPage.content.map((comment) => (
                            <CommentItem
                                key={comment.id}
                                comment={comment}
                                onDeleted={() => loadComments(currentPage)}
                            />
                        ))
                    )}

                    <div className="pagination">
                        <button onClick={handlePrev} disabled={currentPage === 0}>
                            Poprzednia
                        </button>
                        <span className="page-info">
                            Strona {currentPage + 1} z {commentsPage.totalPages}
                        </span>
                        <button
                            onClick={handleNext}
                            disabled={currentPage === commentsPage.totalPages - 1}
                        >
                            Następna
                        </button>
                    </div>
                </>
            )}
        </div>
    );
};

export default CommentsList;
