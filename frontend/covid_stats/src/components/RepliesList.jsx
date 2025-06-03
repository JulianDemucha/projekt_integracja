import React, { useEffect, useState } from 'react';
import { fetchReplies } from '../api/comments';

const RepliesList = ({ parentId }) => {
    const [repliesPage, setRepliesPage] = useState(null);
    const [currentPage, setCurrentPage] = useState(0);
    const [loading, setLoading] = useState(false);

    const loadReplies = async (page) => {
        setLoading(true);
        try {
            const data = await fetchReplies(parentId, page);
            setRepliesPage(data);
            setCurrentPage(data.number);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadReplies(0);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [parentId]);

    const handlePrev = () => {
        if (currentPage > 0) loadReplies(currentPage - 1);
    };

    const handleNext = () => {
        if (repliesPage && currentPage < repliesPage.totalPages - 1) {
            loadReplies(currentPage + 1);
        }
    };

    return (
        <div>
            {loading && <p>Ładowanie odpowiedzi...</p>}

            {!loading && repliesPage && (
                <>
                    {repliesPage.content.map((reply) => (
                        <div key={reply.id} className="border-l pl-2 mb-2">
                            <p className="font-semibold">{reply.author.username}</p>
                            <p>{reply.content}</p>
                            <p className="text-xs text-gray-500">
                                {new Date(reply.createdAt).toLocaleString('pl-PL')}
                            </p>
                        </div>
                    ))}

                    <div className="flex justify-between mt-2 mb-4">
                        <button
                            onClick={handlePrev}
                            disabled={currentPage === 0}
                            className="px-2 py-1 bg-gray-200 rounded disabled:opacity-50 text-xs"
                        >
                            Poprzednie
                        </button>
                        <span className="text-xs">
              Strona {currentPage + 1} z {repliesPage.totalPages}
            </span>
                        <button
                            onClick={handleNext}
                            disabled={
                                repliesPage ? currentPage === repliesPage.totalPages - 1 : true
                            }
                            className="px-2 py-1 bg-gray-200 rounded disabled:opacity-50 text-xs"
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
