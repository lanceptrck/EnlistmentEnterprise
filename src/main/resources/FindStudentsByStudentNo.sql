SELECT * FROM students
LEFT OUTER JOIN enlistments
ON students.student_number = enlistments.student_number
LEFT OUTER JOIN sections
ON enlistments.section_id = sections.section_id
WHERE students.student_number = ?
