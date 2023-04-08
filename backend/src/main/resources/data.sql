    INSERT INTO student(advanced, name, subject, surname, type_of_school, year_of_school) VALUES (false,"Jakub","Matematyka","Królewicz","Studia",3);

    INSERT INTO reservation(day, end_hour, start_hour, student_id) VALUES (CURRENT_DATE, CURRENT_TIME(), CURRENT_TIME() + 1, 1);
    INSERT INTO reservation(day, end_hour, start_hour, student_id) VALUES (CURRENT_DATE, CURRENT_TIME(), CURRENT_TIME() + 1, 1);
    INSERT INTO reservation(day, end_hour, start_hour, student_id) VALUES (CURRENT_DATE, CURRENT_TIME(), CURRENT_TIME() + 1, 1);


    INSERT INTO student(advanced, name, subject, surname, type_of_school, year_of_school) VALUES
                        (false,"Dominik","Matematyka","Januszewski","Szkoła Podstawowa",6);


    INSERT INTO reservation(day, end_hour, start_hour, student_id) VALUES ("2023-04-09", "18:00", "16:30", 2);