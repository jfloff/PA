#lang racket

;;; Projecto

;; Tabela de metodos genericos
(define generic-methods-table
  (make-hash))

;; Tabela de metodos concretos
(define (concrete-methods-table)
  (make-hash))

;; Get Concrete Methods from generic
(define (get-concrete-methods-from-generic name)
  (hash-ref generic-methods-table name))

;; Lista de metodos concretos
(define (concrete-methods-list) (list))

;; Estrutura para representar funções genericas
(struct generic-method (name params) #:mutable #:property prop:procedure (lambda (f . params-list) (generic-function-protocol (get-concrete-methods-from-generic (generic-method-name f)) params-list)))

;; Estrutura para representar métodos
(struct concrete-method (name params-type func) #:mutable)  

;; Regra para definir o comando defgeneric
(define-syntax-rule
  (defgeneric name (params ...))
  (begin
    (define name (generic-method 'name '(params ...)))
    (hash-set! generic-methods-table 'name (concrete-methods-list))))

;; Regra para definir o comando defmethod
(define-syntax-rule
  (defmethod name ((params type) ...) body ...)
  (let ([concrete-methods-list (get-concrete-methods-from-generic 'name)]
        [new-method (concrete-method 'name `(,type ...) (lambda (params ...) body ...))])
    (hash-set! generic-methods-table 'name (cons new-method concrete-methods-list))))

;; Verificar se um método é aplicavel
(define (verify-applicable-method lst params-lst)
  (cond ((and (empty? lst)(empty? params-lst))'#t)
        (((first lst)(first params-lst)) (verify-applicable-method (rest lst) (rest params-lst)))
        (else '#f)))

;; Generic function protocol
(define (generic-function-protocol methods-list params)
  (let ((methods-applicable (list)))
    (define (get-applicable-methods meth-lst params-lst)
      (if (empty? meth-lst)
          methods-applicable
          (if (verify-applicable-method (concrete-method-params-type (first meth-lst)) params-lst)
              (begin
                (set! methods-applicable (cons (first meth-lst) methods-applicable))
                (get-applicable-methods (rest meth-lst) params-lst))
              (get-applicable-methods (rest meth-lst) params-lst))))
    (get-applicable-methods methods-list params)
    methods-applicable))


(defgeneric add (x y))

(defmethod add ((x number?)(y number?)) (+ x y))

(defmethod add ((y string?) (e string?)) (string-append y e))