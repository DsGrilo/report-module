import {useEffect, useState} from "react";

export const useFetchReport = () => {
    const URL = "http://localhost:8080/report";

    const [ reports, setReports ] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetch(URL, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer seu-token-aqui',
            },
        })
            .then((res) => {
                if (!res.ok) {
                    throw new Error(`Erro na requisição: ${res.status} ${res.statusText}`);
                }
                return res.json();
            })
            .then((data) => {
                setReports(data);
                setLoading(false);
            })
            .catch((error) => {
                console.error('Erro na API:', error.message);
                setError(error.message);
                setLoading(false);
            });
    }, [URL]);


    return { reports, loading, error };
}
