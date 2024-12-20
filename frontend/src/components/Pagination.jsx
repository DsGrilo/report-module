import { usePagination } from "../hooks/usePagination.jsx";
import styles from './Pagination.module.css';
import {useMemo} from "react";

export const Pagination = ({
                               onPageChange,
                               totalCount,
                               siblingCount = 1,
                               currentPage,
                               pageSize
                           }) => {
    const totalPages = Math.ceil(totalCount / pageSize);

    const range = (start, end) => {
        const rangeArray = [];
        for (let i = start; i <= end; i++) {
            rangeArray.push(i);
        }
        return rangeArray;
    };

    const paginationRange = useMemo(() => {
        const startPage = Math.max(2, currentPage - siblingCount);
        const endPage = Math.min(totalPages - 1, currentPage + siblingCount);
        return [1, ...range(startPage, endPage), totalPages];
    }, [currentPage, totalPages, siblingCount]);

    return (
        <div className={styles.paginationContainer}>
            {paginationRange.map((page, index) => (
                <button
                    key={index}
                    className={`${styles.paginationItem} ${page === currentPage ? styles.selected : ''}`}
                    onClick={() => onPageChange(page)}
                    disabled={page === currentPage}
                >
                    {page}
                </button>
            ))}
        </div>
    )
};
