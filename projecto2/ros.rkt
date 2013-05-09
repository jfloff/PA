#lang racket

; Hash para representar o grafo
(define type-graph (make-hash))

; Definir um subtipo 
(define (defsubtype child parent)
  (cond ((and (not (hash-has-key? type-graph child))(not (check-possible-cycle child parent))) (hash-set! type-graph child (list parent)))
        ((not (check-possible-cycle child parent)) (hash-set! type-graph child (list parent)))
        (else (error "Wrong definition of subtype"))))

; Definir um subtipo (possivelmente com multiplos super tipos)
(define (def-my-subtype child parent)
  (cond ((and (not (hash-has-key? type-graph child))(not (check-possible-cycle child parent))) (hash-set! type-graph child (list parent)))
        ((not (check-possible-cycle child parent)) (hash-set! type-graph child (cons parent (hash-ref type-graph child))))
        (else (error "Wrong definition of subtype"))))

; Verificar se existe possibilidade de haver um ciclo após a inserção
(define (check-possible-cycle child parent)
  (define (check-cycle-aux child parents-lst)
    (cond ((empty? parents-lst)#f)
          ((eq? child (first parents-lst)) #t)
          ((not (hash-has-key? type-graph (first parents-lst))) #f)
          (else (or (check-cycle-aux child (hash-ref type-graph (first parents-lst)))
                    (check-cycle-aux child (rest parents-lst))))))
  (and (hash-has-key? type-graph parent)
       (check-cycle-aux child (hash-ref type-graph parent))))

;; Estrutura para representar funções genericas
(struct generic-function (name parameters args-order combination-proc) #:mutable #:property prop:procedure (lambda (f . params-list) (generic-function-protocol (get-concrete-methods-from-generic (generic-function-name f)) params-list)))

;; Estrutura para representar métodos
(struct concrete-method (name types func types-ordered combination-proc) #:mutable)  

;; Tabela de metodos genericos
(define generic-functions-table
  (make-hash))

;; Tabela de todos os metodos concretos
(define concrete-methods-table
  (make-hash))

;; Lista de metodos concretos
(define (concrete-methods-list) (list))

;; Get Concrete Methods from generic function name
(define (get-concrete-methods-from-generic name)
  (hash-ref generic-functions-table name))

;; Metodos da funcao generica
(define (generic-function-methods function)
  (hash-ref generic-functions-table (generic-function-name function)))

;; Regra para definir o comando defgeneric
(define-syntax defgeneric
  (syntax-rules ()
    [(defgeneric name (params ...))
     (begin
       (define name (generic-function 'name '(params ...) '(params ...) empty))
       (hash-set! generic-functions-table 'name (concrete-methods-list)))]
    [(defgeneric name (params ...) (#:argument-precedence-order args-order ...))
     (begin
       (define name (generic-function 'name '(params ...) '(args-order ...) empty))
       (hash-set! generic-functions-table 'name (concrete-methods-list)))]
    [(defgeneric name (params ...) (#:method-combination procedure))
     (begin
       (define name (generic-function 'name '(params ...) '(params ...) procedure))
       (hash-set! generic-functions-table 'name (concrete-methods-list)))]
    [(defgeneric name (params ...) (#:argument-precedence-order args-order ...)
       (#:method-combination procedure))
     (begin
       (define name (generic-function 'name '(params ...) '(args-order ...) procedure))
       (hash-set! generic-functions-table 'name (concrete-methods-list)))]))

;; Verificar/Actualizar a ordem de precedencia nos argumentos
(define (check-args-order types params args-order)
  (let ([hash-types (make-hash)])
    (define (check-aux hash args-order)
      (if (empty? args-order)
          empty
          (cons (hash-ref hash (first args-order)) (check-aux hash (rest args-order)))))
    (if (equal? params args-order)
        types
        (begin
          (set! hash-types (insert-hash hash-types params types))
          (check-aux hash-types args-order)))))

;; Inserir cada elemento das listas numa hash
(define (insert-hash hash params types)
  (if (empty? params)
      hash
      (begin 
        (hash-set! hash (first params) (first types))
        (insert-hash hash (rest params) (rest types)))))

;; Remover o metodo da lista de metodos em caso de ser necessaria uma redefinicao
(define (remove-method-from-concrete-list name types methods-list)
  (let ((lst methods-list))
    (define (remove-aux name types methods-list-aux) 
      (if (and (eq? (concrete-method-name (first methods-list-aux)) name)
               (equal? (concrete-method-types (first methods-list-aux)) types))
          (set! lst (remove (first methods-list-aux) methods-list))
          (remove-aux name types (rest methods-list-aux))))
    (remove-aux name types methods-list)
    lst))

;; Regra para definir o comando defmethod
(define-syntax-rule
  (defmethod name ((params type) ...) body ...)
  (let* ([concrete-methods-list (get-concrete-methods-from-generic 'name)]
         [types-ordered (check-args-order `(,type ...) '(params ...) (generic-function-args-order name))]
         [new-method (concrete-method 'name `(,type ...) (lambda (params ...) body ...) types-ordered (generic-function-combination-proc name))]
         [method-key (cons 'name '((params type) ...))])
    (if (not (hash-has-key? concrete-methods-table method-key))
        (begin
          (hash-set! generic-functions-table 'name (cons new-method concrete-methods-list))
          (hash-set! concrete-methods-table method-key '(body ...)))
        (begin
          (set! concrete-methods-list (remove-method-from-concrete-list 'name `(,type ...) concrete-methods-list))
          (hash-set! generic-functions-table 'name (cons new-method concrete-methods-list))
          (hash-set! concrete-methods-table method-key '(body ...))))))

;; Generic function protocol
(define (generic-function-protocol methods-list params)
  (let ((methods-applicable (list)))
    
    ;; Verificar se um método é aplicavel
    (define (verify-applicable-method lst params-lst)
      (cond ((and (empty? lst)(empty? params-lst))'#t)
            ((with-handlers ([exn:fail?
                              (lambda (expr) #f)])
               ((first lst)(first params-lst))) (verify-applicable-method (rest lst) (rest params-lst)))
            (else '#f)))
    
    ;; Devolver todos os metodos aplicaveis
    (define (get-applicable-methods meth-lst params-lst)
      (if (empty? meth-lst)
          methods-applicable
          (if (verify-applicable-method (concrete-method-types (first meth-lst)) params-lst)
              (begin
                (set! methods-applicable (cons (first meth-lst) methods-applicable))
                (get-applicable-methods (rest meth-lst) params-lst))
              (get-applicable-methods (rest meth-lst) params-lst))))
    
    ;; Verificar os metodos especificos
    (define (more-specific-method method-0 method-1)
      (define (more-specific-method-aux types-lst-0 types-lst-1)
        (cond ((and (empty? types-lst-0)(empty? types-lst-1)) #f)
              ((check-possible-cycle (first types-lst-1) (first types-lst-0)) #t)
              ((check-possible-cycle (first types-lst-0) (first types-lst-1)) #f)
              (else (more-specific-method-aux (rest types-lst-0) (rest types-lst-1)))))
      (more-specific-method-aux (concrete-method-types-ordered method-0)(concrete-method-types-ordered method-1)))
    
    ;; Combine the results of executing all the applicable methods
    (define (method-combination procedure list-applicable params)
      (define (apply-method lst params)
        (if (empty? lst)
            empty
            (cons (apply (concrete-method-func (first lst)) params) (apply-method (rest lst) params))))
      (apply procedure (apply-method list-applicable params)))
    
 (cond ((empty? (get-applicable-methods methods-list params)) (error "Method missing for arguments" params))
          ((empty? (concrete-method-combination-proc (first methods-list)))
           (apply (concrete-method-func (first (sort methods-applicable more-specific-method))) params))
          (else (method-combination (concrete-method-combination-proc (first methods-list)) (sort methods-applicable more-specific-method) params)))))

;;;;;;;;;;;;;;
;;; TESTES ;;; 
;;;;;;;;;;;;;;

(defsubtype complex? number?)
(defsubtype real? complex?)
(defsubtype rational? real?)
(defsubtype integer? rational?)
(defsubtype zero? integer?)
(defsubtype odd? integer?)
(defsubtype even? integer?)
(defsubtype zero? even?)

(defsubtype string? char?)

(defsubtype zero? string?)

(defsubtype char? number?)

(defsubtype odd? char?)

(defsubtype integer? string?)

(defgeneric add (x y))

(defmethod add ((x zero?) (y zero?))
  (display "zero?"))

(defmethod add ((x zero?) (y zero?))
  (display "zero?-redefined"))

(defmethod add ((x integer?) (y integer?))
  (display "integer?"))

(defmethod add ((x integer?) (y zero?))
  (display "integer-zero?"))

(defmethod add ((x even?) (y even?))
  (display "even?"))

(defmethod add ((x even?) (y integer?))
  (display "even-integer?"))

(defmethod add ((x complex?) (y complex?))
  (display "complex?"))

(defmethod add ((x real?) (y real?))
  (display "real?"))

(defmethod add ((x string?) (y string?))
  (string-append x y))

(defmethod add ((x string?) (y string?))
  (string-append y x))