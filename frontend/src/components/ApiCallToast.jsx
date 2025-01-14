import React, {useState} from "react";
import Toast from 'react-bootstrap/Toast';


function ApiCallToast( {msg, variant} ) {
    const [showToast, setShowToast] = useState(false);

    return (
        <Toast
            className="d-inline-block m-1"
            bg={variant}
            key='ap-call-result'
        >
            <Toast.Header>
                <img
                    src="holder.js/20x20?text=%20"
                    className="rounded me-2"
                    alt=""
                />
                <strong className="me-auto">Result:</strong>

            </Toast.Header>
            <Toast.Body className={variant === 'Dark' && 'text-white'}>
                {msg}
            </Toast.Body>
        </Toast>
    )
}

export default ApiCallToast;