import React, { useEffect, useState } from 'react';
import { fetchRootComments, postRootComment } from '../api/comments';
import CommentItem from './CommentItem';

const CommentsList = ({ user }) => {
    // Inicjalizujemy stan jako obiekt z pustą tablicą content
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
            // Jeśli zwrócimy obiekt z content:[] nawet przy błędzie,
            // to data.content zawsze będzie tablicą.
            setCommentsPage(data);
            setCurrentPage(data.number);
        } catch (err) {
            console.error('loadComments – nieoczekiwany błąd:', err);
            // W razie czego ustawiamy pustą stronę
            setCommentsPage({ content: [], totalPages: 0, number: 0 });
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
        if (currentPage > 0) {
            loadComments(currentPage - 1);
        }
    };

    const handleNext = () => {
        if (currentPage < commentsPage.totalPages - 1) {
            loadComments(currentPage + 1);
        }
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
            // Błąd HTTP (np. 404 lub 500) pochodzi z axiosa
            console.error('Błąd podczas dodawania komentarza:', err.response || err);
            setErrorAdding('Nie udało się dodać komentarza. Sprawdź konsolę.');
        }
    };

    return (
        <div className="p-4">
            <h2 className="text-xl font-bold mb-2">Komentarze</h2>

            <form onSubmit={handleAddComment} className="mb-4">
        <textarea
            className="w-full border p-2 rounded mb-2"
            rows={3}
            placeholder="Dodaj nowy komentarz..."
            value={newContent}
            onChange={(e) => setNewContent(e.target.value)}
        />
                <button
                    type="submit"
                    className="bg-blue-500 text-white px-4 py-2 rounded disabled:opacity-50"
                    disabled={!user}
                >
                    Dodaj komentarz
                </button>
                {errorAdding && (
                    <p className="mt-2 text-red-500 text-sm">{errorAdding}</p>
                )}
            </form>

            {loading && <p>Ładowanie komentarzy...</p>}

            {!loading && Array.isArray(commentsPage.content) && (
                <>
                    {commentsPage.content.length === 0 ? (
                        <p className="text-gray-600">Brak komentarzy do wyświetlenia.</p>
                    ) : (
                        commentsPage.content.map((comment) => (
                            <CommentItem
                                key={comment.id}
                                comment={comment}
                                user={user}
                            />
                        ))
                    )}

                    <div className="flex justify-between mt-4">
                        <button
                            onClick={handlePrev}
                            disabled={currentPage === 0}
                            className="px-3 py-1 bg-gray-200 rounded disabled:opacity-50"
                        >
                            Poprzednia
                        </button>
                        <span>
              Strona {currentPage + 1} z {commentsPage.totalPages}
            </span>
                        <button
                            onClick={handleNext}
                            disabled={currentPage === commentsPage.totalPages - 1}
                            className="px-3 py-1 bg-gray-200 rounded disabled:opacity-50"
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
