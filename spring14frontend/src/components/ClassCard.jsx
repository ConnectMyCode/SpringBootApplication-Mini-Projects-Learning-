function ClassCard(props)
{
    console.log("ClassCard props:", props);
    return(
        <div>
            <h2>{props.id}</h2>
            <h2>{props.className}</h2>
            <p>{props.description}</p>
            <p>Capacity:{props.capacity}</p>
            <p>Booked:{props.confirmedBookingCount}</p>


            <h3>Schedule</h3>
            {props.schedule && (
                <p>
                    {props.schedule.dayOfWeek}:{" "}
                    {props.schedule.startTime} - {props.schedule.endTime}
                </p>
            )}

            <h3>Trainers</h3>
            {props.trainers && props.trainers.map((trainer)=>(<div>
                <p>Name: {trainer.name}</p>
                <p>Specialization: {trainer.specialization}</p>
            </div> ))}

            <button onClick={()=> props.onBook(props.id)}>Book Class</button>


            <button onClick={() => props.onFavorite(props.id)}>
                Add to Favorites
            </button>

        </div>
    );
}

//It has "props" -> parameter that means this is a UI component and it recieves the informeation from the Component its being called in ... That is: -> " Classes.jsx "
export default ClassCard;