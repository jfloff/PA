#lang racket

;;; Projecto

;; Tabela de metodos genericos
(define generic-methods-table
  (make-hash))

;; Tabela de metodos concretos
(define (concrete-methods-table)
  (make-hash))

;; Estrutura para representar funções genericas
(struct generic-method (name param-number) #:mutable)

;; Estrutura para representar métodos
(struct concrete-method (name params-list body) #:mutable) 
  
;; Gerar uma key para indexar a hashtable
(define (generic-key name param-number)
  (cons name param-number))

;; Regra para definir o comando defgeneric
(define-syntax-rule
  (defgeneric name (params ...))
  (begin
    (define name (generic-method 'name (length '(params ...))))
    (hash-set! generic-methods-table (generic-key 'name (length '(params ...))) (concrete-methods-table))))

;; Regra para definir o comando defmethod
(define-syntax-rule
  (defmethod name ((params type) ...) body ...)
  (let ([concrete-meths (hash-ref generic-methods-table (generic-key 'name (length '((params type) ...))))])
    (hash-set! concrete-meths '(type ...) (body ...))))
 
(define (expr-lambda args exp)
  (lambda (args) exp))