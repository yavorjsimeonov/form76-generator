import { useState } from "react";

export function useToast() {
    const [showToast, setShowToast] = useState(false);
    const [toastMessage, setToastMessage] = useState("");
    const [toastColor, setToastColor] = useState("primary");

    const triggerToast = (message, color = "primary") => {
        setToastMessage(message);
        setToastColor(color);
        setShowToast(true);
    };

    const closeToast = () => setShowToast(false);

    return { showToast, toastMessage, toastColor, triggerToast, closeToast };
}
