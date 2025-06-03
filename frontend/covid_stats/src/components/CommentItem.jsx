import React, { useState, useContext } from 'react';
import RepliesList from './RepliesList';
import { postReply } from '../api/comments';

const CommentItem = ({ comment, user }) => {
    const [showReplies, setShowReplies] = useState(false);
    const [replyContent, setReplyContent] = useState('');
    const [refreshReplies, setRefreshReplies] = useState(false);

    const handleToggleReplies = () => {
        setShowReplies((prev) => !prev);
        if (!showReplies) setRefreshReplies((r) => !r);
    };

    const handleAddReply = async (e) => {
        e.preventDefault();
        if (!replyContent.trim()) return;
        if (!user) return; // Zablokuj, jeśli brak zalogowanego
        await postReply(comment.id, user.id, replyContent.trim());
        setReplyContent('');
        setRefreshReplies((r) => !r);
    };

    // Bezpieczne wyciągnięcie autora (może być undefined)
    const authorName =
        comment.author && comment.author.username
            ? comment.author.username
            : 'Anonim';

    return (
        <div className="border rounded p-3 mb-3">
            <p className="font-semibold">{authorName}</p>
            <p className="mb-2">{comment.content}</p>
            <p className="text-sm text-gray-500 mb-2">
                {new Date(comment.createdAt).toLocaleString('pl-PL')}
            </p>

            <button
                onClick={handleToggleReplies}
                className="text-blue-500 text-sm mb-2"
            >
                {showReplies
                    ? 'Ukryj odpowiedzi'
                    : `Pokaż odpowiedzi (${comment.replies.length})`}
            </button>

            {/* Formularz odpowiadania – zobacz tylko, jeśli user jest zalogowany */}
            <div className="mb-2">
                <form onSubmit={handleAddReply} className="mb-2">
          <textarea
              className="w-full border p-2 rounded mb-1"
              rows={2}
              placeholder="Dodaj odpowiedź..."
              value={replyContent}
              onChange={(e) => setReplyContent(e.target.value)}
          />
                    <button
                        type="submit"
                        className="bg-green-500 text-white px-3 py-1 rounded text-sm disabled:opacity-50"
                        disabled={!user}
                    >
                        Odpowiedz
                    </button>
                </form>
            </div>

            {showReplies && (
                <div className="ml-4 mt-2">
                    <RepliesList parentId={comment.id} key={refreshReplies.toString()} />
                </div>
            )}
        </div>
    );
};

export default CommentItem;
