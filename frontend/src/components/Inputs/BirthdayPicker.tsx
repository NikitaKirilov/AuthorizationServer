import DatePicker from "react-datepicker";
import "./BirthdayPicker.css";
import inputStyles from "./Input.module.css";

type BirthdayPickerProps = {
    className?: string;
    selectedDate: Date | null | undefined;
    onChange: (date: Date | null) => void;
}

const MIN_DATE = new Date("01-01-1920");

export default function BirthdayPicker({className, selectedDate, onChange}: Readonly<BirthdayPickerProps>) {
    return (
        <DatePicker id={"birthday"}
                    name={"birthday"}
                    className={`${inputStyles.textInput} ${className || ""}`}
                        selected={selectedDate} placeholderText={"Выберите дату рождения"}
                        dateFormat={"dd.MM.yyyy"}
                        showYearDropdown
                    minDate={MIN_DATE}
                        scrollableYearDropdown
                    yearDropdownItemNumber={120}
                        onChange={onChange}
                        maxDate={new Date()}
                        portalId={"root"}
                        onKeyDown={(e) => e.preventDefault()}
            />
    );
}
