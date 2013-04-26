#lang racket 

;;;; Começar por fazer as funções sem sorting

;;;; STRUCT
(struct fish (weight color))

(define a-fish (fish 1 'red))

(fish? a-fish)

(fish-color a-fish)

(fish-weight a-fish)

; (set-fish-color! a-fish 'blue) -> error

;;;; MUTABLE STRUCT 
(struct fish-mutable ([weight #:mutable] [color #:mutable]))

(define m-fish (fish-mutable 1 'red))

(fish? m-fish)

(fish-mutable-color m-fish)

(fish-mutable-weight m-fish)

(set-fish-mutable-color! m-fish 'blue)

(set-fish-mutable-weight! m-fish 18)


;;;; STRUCT PROCEDURE

(print "STRUCT PROCEDURE")
(newline)

(struct fish-p (weight color) #:mutable #:property prop:procedure (lambda (f n) (let ([w (fish-p-weight f)])
                                                                                  (set-fish-p-weight! f (+ n w)))))

(define wanda (fish-p 12 'red))

(procedure? wanda)

(fish-p-weight wanda)

(wanda 10)

(fish-p-weight wanda)

;;;; DEFINE SYNTAX RULE => Only what we need for defining defmethod and defgeneric functions

;;; Syntax rule swap
(define-syntax-rule
  ;macro name and pattern variables
  (swap x y)
  ;<template>
  (let ([temp x])
    (set! x y)
    (set! y temp)))
  ;</template>

(define x 10)

(swap wanda x)

(print wanda)
(newline)
(print x)
(newline)

;;;; Syntax rule myprint
(define-syntax-rule
  (myprint params ...)
  (print '(params ...)))

(myprint 5)