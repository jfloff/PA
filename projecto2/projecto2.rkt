#lang racket


;;; Projecto - Diversão no Racket

(struct generic-func (name params) #:mutable #:property prop:procedure (lambda (n method) (let ((method-list (list)))
                                                                                          (set! method-list (cons method-list method))
                                                                                            (method 2 2))))
(define-syntax-rule
  (defgeneric name (params ...))
  (define name 
  (generic-func 'name '(params ...))))

(struct generic-method (name params) #:mutable #:property prop:procedure (lambda (name first second)(+ first second)))

(define-syntax-rule
  (defmethod name (params ...) )
  (name (generic-method 'name '(params ...))))