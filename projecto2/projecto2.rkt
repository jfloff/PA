#lang racket

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; 2º Projecto PA - Grupo 9   ;;;
;;; 56960 - João Loff          ;;;
;;; 64712 - Alexandre Almeida  ;;;
;;; 64870 - Tiago Aguiar       ;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Tabela de metodos genericos
(define generic-functions-table
  (make-hash))

;; Tabela de todos os metodos concretos
(define concrete-methods-table
  (make-hash))

;; Lista de metodos concretos
(define (concrete-methods-list) (list))

;; Estrutura para representar funções genericas
(struct generic-function (name parameters) #:mutable #:property prop:procedure (lambda (f . params-list) (generic-function-protocol (get-concrete-methods-from-generic (generic-function-name f)) params-list)))

;; Estrutura para representar métodos
(struct concrete-method (name types func) #:mutable)  

;; Get Concrete Methods from generic function name
(define (get-concrete-methods-from-generic name)
  (hash-ref generic-functions-table name))

;; Metodos da funcao generica
(define (generic-function-methods function)
  (hash-ref generic-functions-table (generic-function-name function)))

;; Predicados do metodo
(define (method-types method)
  (concrete-method-types method))

;; Gerar uma chave para indexar a tabela
(define (generate-key name parameters)
  (cons name parameters))

;; Verificar se é necessário redefinir o método
(define (already-have-method? method)
  (hash-has-key? concrete-methods-table method))

;; Regra para definir o comando defgeneric
(define-syntax-rule
  (defgeneric name (params ...))
  (begin
    (define name (generic-function 'name '(params ...)))
    (hash-set! generic-functions-table 'name (concrete-methods-list))))

;; Regra para definir o comando defmethod
(define-syntax-rule
  (defmethod name ((params type) ...) body ...)
  (let ([concrete-methods-list (get-concrete-methods-from-generic 'name)]
        [new-method (concrete-method 'name `(,type ...) (lambda (params ...) body ...))]
        [method-key (generate-key 'name '((params type) ...))])
    (if (not (already-have-method? method-key))
        (begin
          (hash-set! generic-functions-table 'name (cons new-method concrete-methods-list))
          (hash-set! concrete-methods-table method-key '(body ...)))
        (begin
          (set! concrete-methods-list (remove-method-from-concrete-list 'name `(,type ...) concrete-methods-list))
          (hash-set! generic-functions-table 'name (cons new-method concrete-methods-list))
          (hash-set! concrete-methods-table method-key '(body ...))))))

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
          (if (verify-applicable-method (concrete-method-types (first meth-lst)) params-lst)
              (begin
                (set! methods-applicable (cons (first meth-lst) methods-applicable))
                (get-applicable-methods (rest meth-lst) params-lst))
              (get-applicable-methods (rest meth-lst) params-lst))))
    (if (empty? (get-applicable-methods methods-list params))
        (error "Method missing for arguments" params)
        (apply (concrete-method-func (first methods-applicable)) params))))

; Verificar se duas listas sao iguais
(define (equal-list? lst1 lst2)
  (cond ((and (empty? lst1)(empty? lst2)) #t)
        ((not (= (length lst1) (length lst2))) #f)
        ((eq? (first lst1) (first lst2)) (equal-list? (rest lst1) (rest lst2)))
        (else #f)))

;; Remover o metodo da lista de metodos em caso de ser necessaria uma redefinicao
(define (remove-method-from-concrete-list name types methods-list)
  (let ((lst methods-list))
    (define (remove-aux name types methods-list-aux) 
      (if (and (eq? (concrete-method-name (first methods-list-aux)) name)
               (equal-list? (concrete-method-types (first methods-list-aux)) types))
          (set! lst (remove (first methods-list-aux) methods-list))
          (remove-aux name types (rest methods-list-aux))))
    (remove-aux name types methods-list)
    lst))