import { Link} from "react-router-dom";
import CurrentUser from "./CurrentUser.jsx";
import Logout from "./Logout.jsx";

function Navbar(props)
{
    return(
        <nav>
            <h2>FitClub</h2>

            <Link to="/login">Login</Link>{" "}
            <Link to="/register">Register</Link>{" "}
            <Link to="/classes">Classes</Link>{" "}
            <Link to="/mybookings">My Bookings</Link>
            <CurrentUser sessionVersion={props.sessionVersion}/>
            <Logout onLogoutSuccess={props.onLogoutSuccess}
            />

        </nav>
    );
}
export default Navbar;



