import React, {MouseEvent} from "react";
import "../FormField/FormField.css";
import "./PrimaryButton.css";

type FormButtonProps = {
    children: React.ReactNode
    onClick: (e: MouseEvent<HTMLButtonElement>) => void;
}

export default function PrimaryButton({children, onClick}: Readonly<FormButtonProps>) {
    return (
        <button className={"primary-button"} onClick={onClick}>{children}</button>
    );
}
