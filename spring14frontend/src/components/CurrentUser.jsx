
import { useState, useEffect } from "react";
import Loading from "./Loading.jsx";
import ErrorMessage from "./ErrorMessage.jsx";

function CurrentUser(props) {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [hasChecked, setHasChecked] = useState(false);

    useEffect( () => {
            checkSession();
    }, [props.sessionVersion] );


    async function checkSession() {
        try {
            setLoading(true);
            setError(null);
            setHasChecked(true);

            const response = await fetch(
                "http://localhost:8080/api/auth/me",
                {
                    credentials: "include"
                }
            );

            if (response.status === 401) {
                setUser(null);
                setError(null);
                setHasChecked(true);
                return;
            }

            if (!response.ok) {
                throw new Error(`Session check failed: ${response.status}`);
            }


            const data = await response.json();
            setUser(data);


            console.log("Current user:", data);
        } catch (error) {
            setError(error.message);
            setUser(null);
        } finally {
            setLoading(false);
        }
    }

    return (
        <div>
            <h2>Current Session</h2>



            {loading && (
                <Loading message="Checking session..." />
            )}

            {error && (
                <ErrorMessage message={error} />
            )}

            {hasChecked && !loading && !error && user && (
                <pre>{JSON.stringify(user, null, 2)}</pre>
            )}
        </div>
    );
}

export default CurrentUser;
