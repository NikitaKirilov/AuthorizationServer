import {ComponentPropsWithoutRef, forwardRef} from "react";
import {PageResponse} from "../../types/PageResponse.ts";
import {ChevronLeft, ChevronRight} from "lucide-react";
import styles from "./TableFooter.module.css";


interface TableFooterProps extends ComponentPropsWithoutRef<"div"> {
    objectName: string;
    notFoundText?: string;
    onNextPage: () => void;
    onPrevPage: () => void;
    pageable: PageResponse<unknown>;
}

const TableFooter = forwardRef<HTMLDivElement, TableFooterProps>(
    ({
         objectName,
         notFoundText = "Ничего не найдено",
         onNextPage,
         onPrevPage,
         pageable,
         className,
         ...props
     }, ref) => {
        const from = pageable.pageable.offset + 1;
        const to = from + pageable.numberOfElements - 1;
        const hasData = pageable.totalElements > 0;
        const pageNumber = pageable.pageable.pageNumber;

        return (
            <div ref={ref} {...props} className={`${styles.footer} ${className || ""}`}>
                <span className={"text-hint"}>
                     {hasData
                         ? `Показано ${from}–${to} из ${pageable.totalElements} ${objectName}`
                         : notFoundText}
                </span>
                <div className={styles.pageSwitcher}>
                    <ChevronLeft onClick={pageNumber === 0 ? undefined : onPrevPage}
                                 className={styles.switchIcon}
                                 size={30}
                    />

                    <div className={styles.numberWrapper}>
                        <span className={styles.pageNumber}>
                            {pageNumber + 1}
                        </span>
                    </div>

                    <ChevronRight onClick={pageNumber < pageable.totalPages - 1 ? onNextPage : undefined}
                                  className={styles.switchIcon}
                                  size={30}
                    />
                </div>
            </div>
        );
    },
);

export default TableFooter;
