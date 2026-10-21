import {useEffect, useState} from "react";
import ErrorMessage from "../components/ErrorMessage.jsx";
import Loading from "../components/Loading.jsx";

function MyBookings(props)
{
    const [bookings, setBookings] = useState([]);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);


    useEffect(()=> {
        async function getBookings() {
            try {
                setError(null);

                const response = await fetch(
                    "http://localhost:8080/api/bookings/me",
                    {credentials: "include"}
                );

                if(response.status ===401){
                    throw new Error("Please log in to view your bookings.");
                }

                if (!response.ok) {
                    throw new Error(`Request failed: ${response.status}`);
                }

                const data = await response.json();
                setBookings(data);
            }
            catch(error){
                console.log(`Error Message: `+error.message);
                setError(error.message);
            }
            finally {
                setLoading(false);
            }
        } getBookings();
    } ,[props.loginVersion]);







 /*   async function getBookings()
    {
        try {
            setBookings([]);
            setError(null);

            const response = await fetch(
                "http://localhost:8080/api/bookings/me",
                {credentials: "include"}
            );

            if(response.status ===401){
                throw new Error("Please log in to view your bookings.");
            }

            if (!response.ok) {
                throw new Error(`Request failed: ${response.status}`);
            }

            const data = await response.json();
            setBookings(data);
        }
        catch(error){
            console.log(`Error Message: `+error.message);
            setError(error.message);
        }
        finally{
            setLoading(false);
        }

    }
*/
    // Intentionally incorrect: called during rendering
    // getBookings();  -> I created this invalid scenario to VERIFY :-> If I call Function directly inside the COmponent body : Each Time conponent renders a request is send to the Api ....

    return(
        <div>
            <h2>My Bookings</h2>

            {loading && (
            <Loading message="Loading your bookings..." />
            )}

            {!loading && error && (
                <ErrorMessage message={error} />
            )}

            {!loading && !error && bookings.length === 0 && (
                <p>You have no bookings.</p>
            )}

            {!loading && !error && bookings.length > 0 && (
                <div>
                    {bookings.map( (booking) =>
                        <div key={booking.id}>
                        <h3>{booking.className}</h3>
                        <p>Booking ID: {booking.id}</p>
                        <p>Booking Date: {booking.bookingDate}</p>
                        <p>Status: {booking.status}</p>
                        </div>  )}
                </div>

            )}

        </div>
    );
}
export default MyBookings;