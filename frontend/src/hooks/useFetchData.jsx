import {useState} from "react";

export const useFetchData = (
    type,
    filters = {}
) => {
    const URL = "http://localhost:8080/report";
    const [data, setData] = useState({});
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const fetchData = async () => {
        setLoading(true);

        await fetch(URL, {
            method: 'POST',
            body: JSON.stringify({
                type,
                filters,
            }),
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then((res) => {
                if (!res.ok) {
                    throw new Error(`Erro na requisição: ${res.status} ${res.statusText}`);
                }
                return res.json();
            })
            .then((data) => {
                setData(data);
                setLoading(false);
            })
            .catch((error) => {
                console.error('Erro na API:', error.message);
                setError(error.message);
                setLoading(false);
            });
    }


    return { data, loading, error, fetchData };
}
