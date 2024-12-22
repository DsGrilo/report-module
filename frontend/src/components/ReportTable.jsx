import styles from './ReportTable.module.css';
import {useEffect, useMemo, useState} from "react";
import {Pagination} from "./Pagination.jsx";
import {useFetchReport} from "../hooks/useFetchReport.jsx";
import {useFetchData} from "../hooks/useFetchData.jsx";

export const ReportTable = () => {
    const PageSize = 15;
    const { reports  , loading, error } = useFetchReport();

    const [ selectedReportType, setSelectedReportType ] = useState('');

    const { data, loading: loadingData, error: errorData , fetchData } = useFetchData(selectedReportType);

    const [columns, setColumns] = useState([]);
    const [values, setValues] = useState([]);

    const [currentPage, setCurrentPage] = useState(1);

    useEffect(() => {
        setColumns(data?.columns);
        setValues(data?.values);
    }, [data]);

    useEffect(() => {
        if(reports && reports.length > 0) {
            const value = reports[0].type
            setSelectedReportType(value);
        }
    }, [reports ])

    const currentTableData = useMemo(() => {
        const firstPageIndex = (currentPage - 1) * PageSize;
        const lastPageIndex = firstPageIndex + PageSize;
        return values?.slice(firstPageIndex, lastPageIndex);
    }, [currentPage, values]);

    return (
        <div>
            <div className={styles.header}>
                <h1>Reports</h1>

                <label className={styles.headerSelect}>
                    <span>Report Type </span>
                    <select name="reports" id="reports" value={selectedReportType} onChange={e => setSelectedReportType(e.target.value)}>
                        {reports && reports.map((report, index) => (
                            <option key={index} value={report.type}>{report.name}</option>
                        ))}
                    </select>
                </label>

                <button value={"enviar"} onClick={fetchData}>Enviar</button>
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
                    {
                        (error || errorData) ? (
                            <h1>Erro ao gerar relatorio ...</h1>
                        ) :
                            (loading || loadingData) ? (
                                <h1>Carregando ...</h1>
                            ) : (
                                currentTableData && currentTableData.map((report, index) => (
                                    <tr key={index}>
                                        { columns.map((col, colIndex) => (
                                            <td key={colIndex}>
                                                { col.type === "TEXT" && report[col.index]
                                                    ? report[col.index].length > 40
                                                        ? report[col.index].slice(0, 40) + "..."
                                                        : report[col.index]
                                                    : report[col.index] ?? "N:A"
                                                }
                                            </td>
                                        ))}
                                    </tr>
                                )))
                    }
                    </tbody>
                </table>
            </div>

            <Pagination
                onPageChange={page => setCurrentPage(page)}
                totalCount={values?.length || 0}
                siblingCount={1}
                currentPage={currentPage}
                pageSize={PageSize}
            />
        </div>
    )
}
