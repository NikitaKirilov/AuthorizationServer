import {ComponentPropsWithoutRef, forwardRef} from "react";
import styles from "./Form.module.css";

interface FormProps extends ComponentPropsWithoutRef<"div"> {
    className?: string;
    error?: string | null,
    width?: "sm" | "md" | "lg";
}

const Form = forwardRef<HTMLDivElement, FormProps>(
    ({children, error, className, width = "md", ...props}, ref) => {
        return (
            <div ref={ref}
                 {...props}
                 className={`${styles.form} ${styles[width]} ${className || ""} ${error ? styles.error : ""}`}
            >
                {children}
                {error &&
                    <div className={styles.errorWrapper}>
                        <span className={styles.errorMessage}>{error}</span>
                    </div>
                }
            </div>
        );
    },
);

export default Form;
