    INSERT INTO student(advanced, name, subject, surname, type_of_school, year_of_school)
    VALUES (false,'Jakub','Matematyka','Królewicz','Studia',3);

    INSERT INTO student(advanced, name, subject, surname, type_of_school, year_of_school)
    VALUES (false,'Dominik','Matematyka','Januszewski','Szkoła Podstawowa',6);

    INSERT INTO tutor(name, surname, birth_day, phone_number, email)
    VALUES ('Jan', 'Kowalski', '1999-01-01', '123456789', 'kowalski@wp.pl');

    INSERT INTO reservation(day, end_hour, start_hour, student_id, tutor_id)
    VALUES ('2023-04-09', '18:00', '16:30', 1, 1);