import { Button } from './Button.jsx';

export function Pagination({ currentPage, totalPages, setCurrentPage }) {

    if (totalPages <= 1) return null;

    return (
        <div className="pagination">
            <Button
                className="btn-primary"
                disabled={currentPage === 0}
                onClick={() => setCurrentPage(p => p - 1)}
            >
                Prev
            </Button>

            <span>Page {currentPage + 1} / {totalPages}</span>

            <Button
                className="btn-primary"
                disabled={currentPage >= totalPages - 1}
                onClick={() => setCurrentPage(p => p + 1)}
            >
                Next
            </Button>
        </div>
    );
}