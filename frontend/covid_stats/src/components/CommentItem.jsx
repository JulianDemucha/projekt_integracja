import React, { useState, useContext } from 'react';
import { ChevronDownIcon, ChevronUpIcon, TrashIcon } from '@heroicons/react/outline';
import RepliesList from './RepliesList';
import { postReply, deleteCommentById } from '../api/comments';
import { AuthContext } from '../context/AuthContext';

const CommentItem = ({ comment, onDeleted }) => {
    const { user } = useContext(AuthContext);
    const [showReplies, setShowReplies] = useState(false);
    const [showReplyBox, setShowReplyBox] = useState(false);
    const [replyContent, setReplyContent] = useState('');
    const [refreshReplies, setRefreshReplies] = useState(false);
    const [deleting, setDeleting] = useState(false);
    const [replyCount, setReplyCount] = useState(comment.replies?.length || 0);

    const authorName = comment.author?.username || 'Anonim';

    // Czy zalogowany może usunąć ten komentarz?
    const canDeleteComment =
        user?.role === 'ROLE_ADMIN' || user?.id === comment.author?.id;

    const handleToggleReplies = () => {
        setShowReplies((prev) => !prev);
        if (!showReplies) {
            setRefreshReplies((r) => !r);
        }
    };

    const handleToggleReplyBox = () => {
        setShowReplyBox((prev) => !prev);
        if (showReplyBox) setReplyContent('');
    };

    const handleAddReply = async (e) => {
        e.preventDefault();
        if (!replyContent.trim() || !user) return;
        try {
            await postReply(comment.id, user.id, replyContent.trim());
            setReplyContent('');
            setRefreshReplies((r) => !r);
            setShowReplies(true);
            setShowReplyBox(false);
            setReplyCount((prev) => prev + 1);
        } catch (err) {
            console.error('Błąd dodawania odpowiedzi:', err);
        }
    };

    const handleDelete = async () => {
        if (!window.confirm('Na pewno chcesz usunąć ten komentarz?')) return;
        setDeleting(true);
        try {
            await deleteCommentById(comment.id);
            onDeleted();
        } catch (err) {
            console.error('Błąd podczas usuwania komentarza:', err);
            alert('Nie udało się usunąć komentarza.');
        } finally {
            setDeleting(false);
        }
    };

    return (
        <div className="comment-item">
            {/* Nagłówek: autor po lewej, data po prawej */}
            <div className="comment-header">
                <p className="comment-author">{authorName}</p>
                <p className="comment-date">
                    {new Date(comment.createdAt).toLocaleString('pl-PL')}
                </p>
            </div>

            {/* Treść komentarza */}
            <p className="comment-content">{comment.content}</p>

            {/* Przyciski pod treścią */}
            <div className="comment-actions">
                <button
                    onClick={handleToggleReplyBox}
                    className="btn-reply-action"
                    type="button"
                    disabled={!user}
                >
                    Odpowiedz
                </button>

                <button
                    onClick={handleToggleReplies}
                    className="btn-toggle-replies"
                    type="button"
                >
                    {showReplies ? (
                        <>
                            <ChevronUpIcon className="inline-icon" />
                            Ukryj odpowiedzi ({replyCount})
                        </>
                    ) : (
                        <>
                            <ChevronDownIcon className="inline-icon" />
                            Pokaż odpowiedzi ({replyCount})
                        </>
                    )}
                </button>

                {canDeleteComment && (
                    <button
                        onClick={handleDelete}
                        disabled={deleting}
                        className="comment-delete-btn"
                        title="Usuń komentarz"
                        type="button"
                    >
                        <TrashIcon className="inline-icon" />
                    </button>
                )}
            </div>

            {/* Inline pole do wpisania odpowiedzi */}
            {showReplyBox && (
                <form className="reply-form-inline" onSubmit={handleAddReply}>
                    <input
                        type="text"
                        className="reply-input-inline"
                        maxLength={500}
                        placeholder="Twoja odpowiedź..."
                        value={replyContent}
                        onChange={(e) => setReplyContent(e.target.value)}
                        disabled={!user}
                    />
                    <button
                        type="submit"
                        className="btn-reply-inline"
                        disabled={!replyContent.trim() || !user}
                    >
                        Wyślij
                    </button>
                </form>
            )}

            {/* Lista odpowiedzi */}
            {showReplies && (
                <div className="replies-container">
                    <RepliesList
                        parentId={comment.id}
                        key={refreshReplies.toString()}
                    />
                </div>
            )}
        </div>
    );
};

export default CommentItem;
