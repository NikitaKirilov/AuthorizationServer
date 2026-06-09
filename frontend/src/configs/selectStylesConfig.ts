import {StylesConfig} from "react-select";

export type SelectOption = {
    value: string;
    label: string;
};


export const selectStylesConfig: StylesConfig<SelectOption> = {
    control: (base, state) => ({
        ...base,

        background: "rgba(255,255,255,0.04)",
        borderRadius: "10px",
        border: state.isFocused ? "2px solid #8b5cf6" : "2px solid rgba(255,255,255,0.08)",

        boxShadow: state.isFocused
            ? "0 0 0 4px rgba(139,92,246,0.15)"
            : undefined,

        transition:
            "border-color 0.2s ease, background 0.2s ease, transform 0.2s ease",

        "&:hover": {
            borderColor: "#e6e2e2",
            boxShadow: "0 0 0 4px rgba(255,255,255,0.06)",
        },
    }),

    valueContainer: (base) => ({
        ...base,
        padding: "4px 12px",
    }),

    input: (base) => ({
        ...base,
        color: "white",
    }),

    placeholder: (base) => ({
        ...base,
        color: "rgba(255,255,255,0.35)",
    }),

    singleValue: (base) => ({
        ...base,
        color: "white",
    }),

    multiValue: (base) => ({
        ...base,
        background: "rgba(139,92,246,0.2)",
        border: "1px solid rgba(139,92,246,0.4)",
        borderRadius: "8px",
    }),

    multiValueLabel: (base) => ({
        ...base,
        color: "white",
        fontSize: "14px",
    }),

    multiValueRemove: (base) => ({
        ...base,
        color: "rgba(255,255,255,0.7)",

        ":hover": {
            backgroundColor: "var(--button-background-hover)",
            color: "white",
        },
    }),

    menu: (base) => ({
        ...base,
        background: "var(--button-background)",
        border: "1px solid rgba(255,255,255,0.08)",
        borderRadius: "10px",
        overflow: "hidden",
    }),

    menuList: (base) => ({
        ...base,
        padding: 0,
    }),

    option: (base, state) => ({
        ...base,

        backgroundColor: state.isFocused
            ? "rgba(139,92,246,0.15)"
            : "transparent",

        color: "white",

        cursor: "pointer",

        ":active": {
            backgroundColor: "rgba(139,92,246,0.3)",
        },
    }),

    dropdownIndicator: (base) => ({
        ...base,
        color: "rgba(255,255,255,0.6)",

        ":hover": {
            color: "white",
        },
    }),

    clearIndicator: (base) => ({
        ...base,
        color: "rgba(255,255,255,0.6)",

        ":hover": {
            color: "white",
        },
    }),

    indicatorSeparator: () => ({
        display: "none",
    }),
};
