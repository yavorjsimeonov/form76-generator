import React, {useState} from "react";


// function Toast( {msg, color} ) {
//     const [showToast, setShowToast] = useState(false);
//
//     return (
//         <div className="toast-container">
//             {showToast && (
//                 <div
//                     className="toast align-items-center text-bg-primary border-0 show"
//                     role="alert"
//                     aria-live="assertive"
//                     aria-atomic="true"
//                 >
//                     <div className="d-flex">
//                         <div className="toast-body">
//                             Report generation triggered successfully. You will receive an email on example@example.com.
//                         </div>
//                         <button
//                             type="button"
//                             className="btn-close btn-close-white me-2 m-auto"
//                             aria-label="Close"
//                             onClick={() => setShowToast(false)}
//                         ></button>
//                     </div>
//                 </div>
//             )}
//         </div>
//     )
// }

function Toast({ show, message, color = "primary", onClose }) {
    return (
        <div
            className={`toast align-items-center text-bg-${color} border-0 ${show ? "show" : "hide"}`}
            role="alert"
            aria-live="assertive"
            aria-atomic="true"
            style={{ position: "fixed", top: "7rem", right: "2rem", zIndex: 1050 }}
        >
            <div className="d-flex">
                <div className="toast-body">{message}</div>
                <button
                    type="button"
                    className="btn-close btn-close-white me-2 m-auto"
                    aria-label="Close"
                    onClick={onClose}
                ></button>
            </div>
        </div>
    );
}

export default Toast;
