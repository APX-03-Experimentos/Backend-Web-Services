@US003 @invitaciones @grupos
Feature: Invitación a grupos
  Como profesor
  Quiero ser capaz de invitar a mis alumnos a los grupos creados por mí
  Para facilitar su incorporación a las actividades del curso

  Scenario: Estudiante ingresa con código de invitación válido
    Given un grupo creado con código de invitación "INV123"
    When un estudiante ingresa el código "INV123" en la plataforma
    Then el sistema añade al estudiante al grupo
    And registra la acción
    And envía notificación automática al profesor confirmando la incorporación

