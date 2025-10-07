@US015 @entregas @profesor
Feature: Acceso a todas las entregas de un trabajo
  Como profesor
  Quiero acceder en una sola vista a todas las entregas de un trabajo
  Para agilizar la revisión y calificación

  Scenario: Ver listado completo de entregas
    Given un profesor en la página de administración del trabajo "Tarea de Álgebra"
    When selecciona la pestaña "Entregas"
    Then el sistema muestra lista completa de estudiantes con:
      | estudiante      | estado      | hora_entrega    | archivo        |
      | Ana García      | Entregado   | 2024-12-14 10:30| tarea_ana.pdf  |
      | Carlos López    | Pendiente   | -               | -              |
      | María Torres    | Calificado  | 2024-12-13 15:45| tarea_maria.pdf|

