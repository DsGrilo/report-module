import styles from './ReportTable.module.css';
import React, {useContext, useMemo, useState} from "react";
import {Pagination} from "./Pagination.jsx";
import {useFetchReport} from "../hooks/useFetchReport.jsx";
import {ReportSelectedContext} from "../context/ReportSelectedContext.jsx";
import {useFetchData} from "../hooks/useFetchData.jsx";

export const ReportTable = () => {
    const PageSize = 10;
    const { reports  , loading, error } = useFetchReport();
    const { selectedValue, handleSelectChange } = useContext(ReportSelectedContext);


    let data = [];
    let columns = [];

    const [currentPage, setCurrentPage] = useState(1);


    const currentTableData = useMemo(() => {
        const firstPageIndex = (currentPage - 1) * PageSize;
        const lastPageIndex = firstPageIndex + PageSize;
        return data.slice(firstPageIndex, lastPageIndex);
    }, [currentPage]);


    const handleReport = async (e) => {
        e.preventDefault();
    }

    return (
        <div>
            <div className={styles.header}>
                <h1>Reports</h1>

                <label className={styles.headerSelect}>
                    <span>Report Type </span>
                    <select name="reports" id="reports" value={selectedValue} onChange={handleSelectChange}>
                        {reports && reports.map((report, index) => (
                            <option key={index} value={report.type}>{report.name}</option>
                        ))}
                    </select>
                </label>

                <button value={"enviar"} onClick={handleReport}>Enviar</button>
            </div>

            <div className={styles.report}>
                <table className={styles.table}>
                    <thead>
                    <tr>
                        {columns &&
                            columns.map((column) => (
                                <th key={column.key}>{column.name}</th>
                            ))}
                    </tr>
                    </thead>
                    <tbody>
                    {currentTableData && currentTableData.map((report, index) => (
                        <tr key={index}>
                            { columns.map((col, colIndex) => (
                                <td key={colIndex}>{report[col.index]}</td>
                            ))}
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>

            <Pagination
                onPageChange={page => setCurrentPage(page)}
                totalCount={data.length}
                siblingCount={1}
                currentPage={currentPage}
                pageSize={PageSize}
            />
        </div>
    )
}
