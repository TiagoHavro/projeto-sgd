/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sistemaM.persistencia;

import java.io.Serializable;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 *
 * @author tiago
 */

@Transacional
@Interceptor
public class TransactionInterceptor implements Serializable{

    @Inject
    private EntityManager em;

    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        EntityTransaction transaction = em.getTransaction();
        boolean create = false;
        try {
            if (!transaction.isActive()) {
                transaction.begin();
                transaction.rollback();
                transaction.begin();
                create = true;
            }
            return ctx.proceed();
        } catch (Exception ex) {
            if (transaction != null && create) {
                transaction.rollback();
            }
            throw ex;
        } finally {
            if (transaction != null && transaction.isActive() && create) {
                transaction.commit();
            }
        }
    }

}
