import { useState, useEffect } from "react";
import { form76GeneratorApi } from "../api/Form76GeneratorApi";

export function useLocationDetails(id, user) {
    const [location, setLocation] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchLocation = async () => {
            try {
                const response = await form76GeneratorApi.getLocation(user, id);
                setLocation(response.data);
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };
        fetchLocation();
    }, [id]);

    return { location, setLocation, loading, error };
}
