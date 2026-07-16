import {ComponentPropsWithoutRef, forwardRef} from "react";
import styles from "./GridRow.module.css";

interface GridRowProps extends ComponentPropsWithoutRef<"div"> {
    className?: string;
}

const GridRow = forwardRef<HTMLDivElement, GridRowProps>(
    ({children, className, ...props}, ref) => (
        <div ref={ref} {...props} className={`${styles.gridRow} ${className || ""}`}>
            {children}
        </div>
    ),
);

export default GridRow;
