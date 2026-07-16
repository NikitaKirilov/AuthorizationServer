import {ComponentPropsWithoutRef, forwardRef} from "react";
import styles from "./Page.module.css";

interface PageProps extends ComponentPropsWithoutRef<"div"> {
    className?: string;
}

const Page = forwardRef<HTMLDivElement, PageProps>(
    ({children, className, ...props}, ref) => {
        return (
            <div ref={ref} {...props} className={`${styles.page} ${className || ""}`}>
                {children}
            </div>
        );
    },
);

export default Page;
