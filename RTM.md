# RTM — Matriz de Rastreabilidade de Requisitos
## BiblioEAL — Personal Library Manager

> **Definição:** A RTM (Requirements Traceability Matrix) mapeia cada requisito funcional do sistema ao(s) teste(s) que o validam, garantindo cobertura completa. Nenhum requisito existe sem teste; nenhum teste existe sem requisito.

---

## Índice de Requisitos

| ID   | Requisito                         | Tipo           | Testes Vinculados                                                              | Diagrama UML          | Status |
|------|-----------------------------------|----------------|--------------------------------------------------------------------------------|-----------------------|--------|
| RF01 | Cadastro de Usuário               | Funcional      | `shouldRegisterUserSuccessfully`                                               | [📊 Ver diagrama](https://mermaid.live/view#pako:eNqdkkFuwjAQRa9ieZVKUPZZIDXQXatWotl1Y8VDYsnx0LEDRYjDcBYu1gkxiABqUbNK8p__fM_MRhaoQabSw1cDroCpUSWp-tMJflQRkETuG0UGu18LRcEUZqFcEBnhygNdC09NqCboAqG1t_ScT82AlqaAa_EVXYnTrBNi7eF4HIul4p2Ag1Yg5kh1Y_e7U7aIMNwPwGfeZh9iRFAaH4DExqkaBqJhuHuDWhk74Bjer5D0tvPru7DtWe5UHN0SHfDhmPakMx0vkgr4ZtBn6zzWSyIegeGl81xZD3c5Pre5_23Xo7IJrRfhsVK-So59-P1aXi0haXv4R_32g2G7xGu7G7PKTyt10f_zJSDQhqAIYmSxNK4__0OAw97w5Pc71pXQIF5aUg5kDcRt0zLdyFBB3W6_hrlqbJDb7Q-kYRFN) | ✅ |
| RF02 | Senha com hash BCrypt             | Segurança      | `shouldStorePasswordAsHash`                                                    | [📊 Ver diagrama](https://mermaid.live/view#pako:eNqVksFKxDAQhl9lyGmFLrjrrYc9rOtREURPIgzJbBtoMnWSVGTZp_HgA_gIfTG7rUilFTSnwJ__m_8fclCaDalcBXpO5DXtLBaC7tFDd2qUaLWt0Ue4DyR3JI3VNBW3l_Jax1sM4YXFXPkTVKbPrtkXvNsOwgi43GxmCTlQf1kE8iWu1hdng_WGIwE3JPCLrSBBCFhFwIowth9iOQOsK6sR1k-rcxBO3oQBNwtZdqFGEXMoMZQ92PBcga9ueTe2oUXqNNDsetck9Q_uA4ndd7na9_aNwTNUXFj_jx05jLqk8L2kbDz0b92iJFKZciQOrVH5QcWS3OlfGNpjqqI6Hj8Bg6TFCA) | ✅ |
| RF03 | Username e email únicos           | Funcional      | `shouldThrowExceptionOnDuplicateUsername`, `shouldThrowExceptionOnDuplicateEmail` | [📊 Ver diagrama](https://mermaid.live/view#pako:eNq9kT1Ow0AQha8y2gokzAFcRBAlSCmgQXQ0o_XD2Wh3x6xnTVCUw3AWLoZ_guQICiqmnPe-N3qag7FSwZSmxWtGtFg5rhOH50j9NJzUWddwVHpqkR6ROmfxU7yXWMtqOQkzZ7FYnKSSsHettsv3QY4ccLETlssJOZmK3j-jS9KU8VvouWmb5I023qNmf5vqHBB1vbdo1Emkgr4v0u7zgxAotzKFPoiCpEOis8A7n_dC7HXA1HXSZyCw81TlxjvLlfyp6Xpgxpo3I35tJfxL4fHwrK25MgGpX1amPBjdIgwvr_DC2as5Hr8A8ua25Q) | ✅ |
| RF04 | Confirmação de senha              | Funcional      | `shouldThrowExceptionWhenPasswordsDontMatch`                                   | [📊 Ver diagrama](https://mermaid.live/view#pako:eNqNUU1KQ0EMvkqYlYJe4EELRV24E8SdmziTzgvMzzPJtELpaTyKF3PkVWiroFnmy_dHds7XQG5wSq-NiqdbxiiYnwv0mVCMPU9YDFbNxptaTGpKJD_xJyV5JNmwpxk8JVwvl0cXAwhFViO5CFYvZ8IRfn69wcQBu5_qtkqAxQJ8LWuW_HBYzRKYDJTKiArl473CCxoduvzhYKPULdz3qBHTSmLLVOzuzdNkXMvvEl3jtOQAZwxKSt-BODZk_VeYSaoqxUa9ZQaPAbU7HCRLcFcuk2Tk4Iads5Hy1wcDrbElc_v9J8VopLo) | ✅ |
| RF05 | Autenticação de usuário           | Funcional      | `shouldLoadUserByUsernameForAuthentication`                                    | [📊 Ver diagrama](https://mermaid.live/view#pako:eNqNUstuwjAQ_JWVTyBBuefAIXDopSpSyK2Xrb0kqyZ2ajtUCPExVT-FH6tDDAqPSvXNnpmd0Y73QhpFIhGOPlvSkpaMhcX6TUM4KL2xkLsWLZv-qUHrWXKD2kNqzZcjew9kjWVdZCRby353j-dBlZHdsqR78MXowizTHoje0_k8miWguGCP0IaLxpqAwJEusedHVuBfZ0hg9ZqtYVaZgnVPvSYExSBVApVB1T2kuzwajc6O43O0Cz2IY-oENqzVQBTJEZ7e2uSX_Q3HPYjfwUvyyJX7I_6tIl3YXeOfavSyJDc6LWkCJbpy_M8J0jJCRs4dfww8r9erbtdYbREWxnwwPRwzrMqSYkvSw-w9CNx1RadVnNoN5Ry_Qy8YeWIiarI1shLJXviS6u6HKtpgW3lxOPwC9Kf0Ow) | ✅ |
| RF06 | Isolamento de dados por usuário   | Segurança      | `shouldListOnlyOwnerBooks`, `shouldThrowExceptionWhenAccessingAnotherUserBook` | [📊 Ver diagrama](https://mermaid.live/view#pako:eNqVU0FuwjAQ_MrKJyqRco8EUlJQhdSWSqW3Xlx7CVYdL3XsUIR4DG_hY3VIgNJGKvjm3RnP7oy8ZoIkspgV-OnRCBwqnlmevxkIhwtHFl4Lz62ipK4tuHVKqAU3DlKijzsyzpLWaNv7L2hLJfBv85FMRsO0bhw0osHg_NEY7kdT6L2HYlFDz_sNoVGJYaaMTLROV5OlQdvxBdrkVsmbE7eBBmIzQU0CMhKBKtZYQh_2zJrV4KLfWg-qtFSApDD-EfxTomWb6j6cTtp3CfiDETE877aZMhwE5aDblJ7IhYFLPCUUwxSN406VHMIyXGBRcFuzqwL5oBQ287ttgF_hfE_JaP9KlPZQKndpEulqLBMjJ7WrHSW7cG0iSvb3yikc0-lfkI3xWrcpnIHc3NISxmH6jOvEZj4P9o2-BC6cIvN_RDZ4YVG4xiXWZTnanCvJ4jVzc8yrnyVxxr12bLP5BgbCMPk) | ✅ |
| RF07 | Cadastro de livro (CREATE)        | Funcional      | `shouldCreateBookSuccessfully`, `shouldCreateBookAndRedirect`                  | [📊 Ver diagrama](https://mermaid.live/view#pako:eNqFUstOwzAQ_JWVT0VKxT2Hij4ulUBFauHEZRtvWwvHLutNClT9GL6FH8OhliA0LTlEWu_M7HjWe1V4TSpXgV4qcgVNDK4ZyycH8cNCPMNDqJCNPx5tkcUUZotOYMR-F4g7Gt4_j70T9tae68-Ja1PQafPOu7WfjI6NNLs_GKRhOdwzRaMbgpXnsrKfH7EPmsCampPJhG1YLSeRPJsv4HoZTwMUvgQxYikDrGTjOQMTli5JtIgdSjePaI2GuvkjTBazC7x01xwKJhTqafEZ-J0jnuqrH16CRVLKIAd6NUHC6G0ajQ2dnh05vURKsP7fMSu0gbp0W6hAkvSAmmL8bU4P5aKjgDX1mgD_8dAUEWxrfyrXEWdTnwvx9_aZtGEqJC2xve8ITA8mh9sYHAJKFRf0jhpVpkriEo1W-V7Jhsrm2WtaYWVFHQ5fxv4N3w) | ✅ |
| RF08 | ISBN único por usuário            | Regra negócio  | `shouldThrowExceptionOnDuplicateIsbnSameOwner`, `shouldAllowSameIsbnForDifferentOwners` | [📊 Ver diagrama](https://mermaid.live/view#pako:eNqlkTFOw0AQRa8y2iqR7BpwEclWQHJBKNLSbLwTZ5XdHTM76xhFOQxn4WIYnCIRCAqm_fP-n685qoYMqkJFfEkYGlxa3bL2zwHG6TSLbWyng0BFtF8j97bB7-IjhZaW1SRcbOaLxVkqAAcbJVavddyEMpinQ0Cuzezu5jaDFJHL-YSfgXxkL5wKEE74U8D10o7pALVz2GpXcps8BrkfGuzEUoAc6nW1ApM6ZxttaPJbkSBQjwxXXg8uDQTaCXLQYnsacY_R05dJBpSEaTw9vb-xpX91r_7ovtUu4q8JUfc424zaXGXKI3ttjSqOSnboP_9rcKuTE3U6fQCBUKuj) | ✅ |
| RF09 | Listagem de livros (READ)         | Funcional      | `shouldReturn200WithBookList`                                                  | [📊 Ver diagrama](https://mermaid.live/view#pako:eNp1ksFKAzEQhl9lyEnBYvEYsNB1RcWWFVxvXmIyXYPZTE2yLVL6MD6LL2bCBu3aNYdA8n___JMhOyZJIePM43uHVmKpReNE-2whLiEDOXjynXCa-qu1cEFLvRY2QOFo69GNCERvV2SDI2P-0x_RbbTEY3FJtqGy6IWcPZnNchiHuUTvBZy_xCK-p7KWqEEyh5vrekgO9GzIrXBYaavmxhQf1daiO6G036nTX2smoy932XuArELIOFxC9zOVjE3-Ji20DwKiyeiNI3-cMPKWdC7ravwlhxO6mE6hugdJbSyfcm7r5WI4qYjn0XJ4-PpstBWZT-2wM9aia4VWjO9YeMU2fRGFK9GZwPb7b14zwt0) | ✅ |
| RF10 | Busca por título                  | Funcional      | `shouldSearchBooksIgnoringCase`                                                | [📊 Ver diagrama](https://mermaid.live/view#pako:eNp1UttOAjEQ_ZVJnzSBSHzcRIyAEeKFBPHBxJfSHZaJux2cdpcQ4if5Ff6YXbZREOhbO-ecOWemG2U4RZUohx8lWoMD0pno4s1CONp4FnhxpRbi5mmpxZOhpbYeesIrh3KkwPzeZ-uF8_xU_RmlIoOHxUe2GQ96TSH2bne7sVkCKWXkNSy0yBqshlnpjG7QEVOj9xwkcHc7hYtZeHTXDrWYxdWWH2l74MiO_hJo8L31lHyOZ1taC3hlUUbp-Z9AxAd2DJDAnGwaebW8Jks2G2WWBfvandB6Yo_AFQr86mwTggmUNlmH1pGnCkEwbCcMYNjIDG8mk9cW7OSKAu3_iXKqhB2EddehdcruMMaRGdb3wXTsjg5td0OXnQ6M78FwETy6Mvc7LeKGAjyuNoGHxs6c8sYMLMOn899fvsxZtVSBUmhKVbJRfoFF_VlTnOsgqz4_fwCtI_cg) | ✅ |
| RF11 | Filtro por status de leitura      | Funcional      | `shouldFilterByEachReadingStatus` (parametrizado)                              | [📊 Ver diagrama](https://mermaid.live/view#pako:eNp1UkFOwzAQ_MrKJ5BaUXGMRBElCEUURaLlxmWxt8Ei8QbbaYWq_h2ncURTgk-2d2ZnZu29kKxIJMLRV0NGUqqxsFi9GQgLpWcLr65Bq7m7qtF6LXWNxsPC8s6RHSkwf96z8ZbL8r_6iuxWS_pbfGZTcLroClF7Op9HsQQclSQ1G4SNLoMELLM079AR06IHDhJ4fFjD1Xu4dLfOo2_czQlrgI3kaC8JIkYtvldH0p1R-c6QvWjJE-B2n6nL3zaRFXrEFD3_hVBpUwzbZGq0UaROz52UemvZgeQKugwwzNCLj8Rvz-k6d93IULEbjX465uvZDPKno1oUPufGYQdSfKUEljo464EIdfg9nVUxERXZCrUSyV74D6raT6dog03pxeHwAzqY4PY) | ✅ |
| RF12 | Edição de livro (UPDATE)          | Funcional      | `shouldUpdateBookSuccessfully`, `shouldThrowExceptionWhenUpdatingAnotherUserBook` | [📊 Ver diagrama](https://mermaid.live/view#pako:eNqNUk1vwjAM_StWTkwCcc8BiY4L0jYmMW67eLFh0dq4S1LQhvjvS2m0qhv7yC32e89-to_KCLHSKvBrw87wwuLOY_XoID00UTxsQoPeSheq0UdrbI0uQuHlENhfSIi8XIuLXsryp_ya_d4a_p68FbeTRdElconJbDbU1HC_Wj_A9ClFw_Ro6QRGKjBY1RIAY4OlfUeSkGUG5KyWO9DQ1ISRR5bGQFHGIAfHfklXPTdDEzF3p2FrHRVvS5o7WnX4s8CQm9GTrxXbD6Rxty2lLi8VGuD3yQ4hLNfFHfCnvWz3T3bguDlbpHkc_e4q4J5H7VD_Y6Af88Upt5xufRo8k_VsYt7YcLkJmG9Mw40NEXtlVGNVsa_QktJHFZ-5aq-VeItNGdXp9AF4HvP3) | ✅ |
| RF13 | Exclusão de livro (DELETE)        | Funcional      | `shouldDeleteBookSuccessfully`, `shouldDeleteBookAndRedirect`                  | [📊 Ver diagrama](https://mermaid.live/view#pako:eNqFUs1OAjEQfpVJT5hAuPdAIhATEg1G9OaltgNM2Haw7S4awtP4KL6Y3ewkuoraWzvf38z0qCw7VFolfK4xWJyT2UTjHwOUY2zmCA-pNpG4e9qbmMnS3oQM08iHhPFMgXk345AjV9Vv9RXGhiz-LN5w2PB82hXEezSZiJkGy2FN0RvAF1vV6f1NkgmghfbsNdwuV_cwfiqvaXwkdxo7rDBjUfIwW91dQeYdBlHpcUVMsmroiANyQ-BDwLhwF580QRWOtKBhTcFNXxfuMrhlhx8IQSCj7w7tBcoi2gjG8Z_qkqZt7B9V3p3t7utYIzqKaLMMqj_TApRNaLimlA0k9MBQURO5UD035FgNlceyGXJKH1Xeom9_lsO1qausTqcPVODW6A) | ✅ |
| RF14 | Proteção de rotas (autenticação)  | Segurança      | `shouldRedirectToLoginWhenNotAuthenticated`                                    | [📊 Ver diagrama](https://mermaid.live/view#pako:eNqNkcFKAzEQhl9lyFmp6C2HHkqLVESFSE-9jMl0Hdwk6yRbKaUPU3yUfTFTdikW92CO83_fZCbZKxsdKa0SfbYULM0ZK0G_DlAO2hwFVpw4Y8jUFxuUzJabUoGZxK9E8jcwjXCoDNlWOO_6_NznejodRF2uoJQQJm8xfiRI5KGOFYfeGKjCX_bTcL94HZyevMxHhC0Jb9giPJiFMcvnp-X8n6YpA3bfEThsu2PNDke93zvd3dyCkGMhm2Eysk-Bz4-h4aU7FgLBETyeWHWlPIlHdkrvVX4nf_ogRxts66wOhx-2IppQ) | ✅ |
| RF15 | Sessão gerenciada (login/logout)  | Funcional      | `SecurityConfig` + `shouldShowLoginPage`                                       | [📊 Ver diagrama](https://mermaid.live/view#pako:eNqNkTFOAzEQRa8ycg2i3yIFBKQgRBCGjmZkD8soa88ytkEoymEQR8nFcFgXrEiBy_n_fft7tsaJJ9OZRK-FoqMlY68YniLUgy6LwmMqqCzTaETN7HjEmOFc5T2R_hXsqBx7S64o549Jbymni0XDOnADOwQKYJFbStOqa57Rwd3aPsDZIL2UDE4CXNj7K8iyoTihc-BIAsc3HNgjWEpp_yX_xHDEHuHaXlq7Wt-ulvVy2TAdpX-3U_Ks5PLPoznO-1Vj-5DabP9ZdQRPcHNwmhMTSAOyN93W5BcKhwV5esYyZLPbfQP0rplo) | ✅ |
| RF16 | Validação de formulários          | Funcional      | `shouldReturnFormWithErrorsWhenTitleEmpty`                                     | [📊 Ver diagrama](https://mermaid.live/view#pako:eNptkkFOAzEMRa9iZQ2iYjmLCg0VGyQGQWHFxp24bdQkLokDElUPwwE4RS9GZiYVjDTZ2f7P_8vKQbWsSVUq0nsi39LC4Cage_OQH7bCAV5iwmB4aO0xiGnNHr1AHfgzUpgYMO9u2UtgayfnhP4VrdEohv0wLy6X83lZW0FMK0dCsObgoGUHcvqRZBk-8Oucp4g7bGRawWPzvISrVe7GATZiaYSOgG7DKFYFN33R6xbLpkAjzRT0wFJb9LuzoWGLmifpidS18dr4zRPFZKXPTSFwnEz8_1bXsxk09z3Qn4tGXDlSBsqZK7jLqmRP37n4swFNUbDNeaO6UI6CQ6NVdVCyJdd9E01rzMHU8fgLQwTJdw) | ✅ |

---

## Detalhamento por Requisito

---

### RF01 — Cadastro de Usuário

**Descrição:** O sistema deve permitir que novos usuários se cadastrem informando nome, username, email e senha.

**Teste:** `UserServiceIntegrationTest::shouldRegisterUserSuccessfully`  
**Tipo:** Integração (Testcontainers + MongoDB real)

**[📊 Ver Diagrama UML de Sequência](https://mermaid.live/view#pako:eNqdkkFuwjAQRa9ieZVKUPZZIDXQXatWotl1Y8VDYsnx0LEDRYjDcBYu1gkxiABqUbNK8p__fM_MRhaoQabSw1cDroCpUSWp-tMJflQRkETuG0UGu18LRcEUZqFcEBnhygNdC09NqCboAqG1t_ScT82AlqaAa_EVXYnTrBNi7eF4HIul4p2Ag1Yg5kh1Y_e7U7aIMNwPwGfeZh9iRFAaH4DExqkaBqJhuHuDWhk74Bjer5D0tvPru7DtWe5UHN0SHfDhmPakMx0vkgr4ZtBn6zzWSyIegeGl81xZD3c5Pre5_23Xo7IJrRfhsVK-So59-P1aXi0haXv4R_32g2G7xGu7G7PKTyt10f_zJSDQhqAIYmSxNK4__0OAw97w5Pc71pXQIF5aUg5kDcRt0zLdyFBB3W6_hrlqbJDb7Q-kYRFN)**

**Fluxo:** Usuário preenche formulário → `POST /register` → `AuthController` → `UserService.register(dto)` → verifica duplicidade de username e email → BCrypt hash da senha → `save(user)` no MongoDB → redirect `/login`.

---

### RF02 — Senha com Hash BCrypt

**Descrição:** Senhas nunca devem ser armazenadas em texto puro. O sistema usa BCrypt para hashing.

**Teste:** `UserServiceIntegrationTest::shouldStorePasswordAsHash`  
**Tipo:** Integração (Testcontainers + MongoDB real)

**[📊 Ver Diagrama UML de Sequência](https://mermaid.live/view#pako:eNqVksFKxDAQhl9lyGmFLrjrrYc9rOtREURPIgzJbBtoMnWSVGTZp_HgA_gIfTG7rUilFTSnwJ__m_8fclCaDalcBXpO5DXtLBaC7tFDd2qUaLWt0Ue4DyR3JI3VNBW3l_Jax1sM4YXFXPkTVKbPrtkXvNsOwgi43GxmCTlQf1kE8iWu1hdng_WGIwE3JPCLrSBBCFhFwIowth9iOQOsK6sR1k-rcxBO3oQBNwtZdqFGEXMoMZQ92PBcga9ueTe2oUXqNNDsetck9Q_uA4ndd7na9_aNwTNUXFj_jx05jLqk8L2kbDz0b92iJFKZciQOrVH5QcWS3OlfGNpjqqI6Hj8Bg6TFCA)**

**Por que BCrypt?** É lento propositalmente (custo configurável), inclui salt automático e é o padrão da indústria para senhas em aplicações web.

---

### RF03 — Username e Email Únicos

**Descrição:** Não é permitido cadastrar dois usuários com o mesmo username ou email.

**Testes:** `shouldThrowExceptionOnDuplicateUsername`, `shouldThrowExceptionOnDuplicateEmail`  
**Tipo:** Integração (Testcontainers + MongoDB real)

**[📊 Ver Diagrama UML de Sequência](https://mermaid.live/view#pako:eNq9kT1Ow0AQha8y2gokzAFcRBAlSCmgQXQ0o_XD2Wh3x6xnTVCUw3AWLoZ_guQICiqmnPe-N3qag7FSwZSmxWtGtFg5rhOH50j9NJzUWddwVHpqkR6ROmfxU7yXWMtqOQkzZ7FYnKSSsHettsv3QY4ccLETlssJOZmK3j-jS9KU8VvouWmb5I023qNmf5vqHBB1vbdo1Emkgr4v0u7zgxAotzKFPoiCpEOis8A7n_dC7HXA1HXSZyCw81TlxjvLlfyp6Xpgxpo3I35tJfxL4fHwrK25MgGpX1amPBjdIgwvr_DC2as5Hr8A8ua25Q)**

---

### RF04 — Confirmação de Senha

**Descrição:** O sistema deve validar que `password` e `confirmPassword` sejam iguais antes de prosseguir.

**Teste:** `UserServiceIntegrationTest::shouldThrowExceptionWhenPasswordsDontMatch`  
**Tipo:** Integração (Testcontainers + MongoDB real)

**[📊 Ver Diagrama UML de Sequência](https://mermaid.live/view#pako:eNqNUU1KQ0EMvkqYlYJe4EELRV24E8SdmziTzgvMzzPJtELpaTyKF3PkVWiroFnmy_dHds7XQG5wSq-NiqdbxiiYnwv0mVCMPU9YDFbNxptaTGpKJD_xJyV5JNmwpxk8JVwvl0cXAwhFViO5CFYvZ8IRfn69wcQBu5_qtkqAxQJ8LWuW_HBYzRKYDJTKiArl473CCxoduvzhYKPULdz3qBHTSmLLVOzuzdNkXMvvEl3jtOQAZwxKSt-BODZk_VeYSaoqxUa9ZQaPAbU7HCRLcFcuk2Tk4Iads5Hy1wcDrbElc_v9J8VopLo)**

---

### RF05 — Autenticação de Usuário

**Descrição:** Usuários cadastrados devem conseguir fazer login com username e senha.

**Teste:** `UserServiceIntegrationTest::shouldLoadUserByUsernameForAuthentication`  
**Tipo:** Integração (Testcontainers + MongoDB real)

**[📊 Ver Diagrama UML de Sequência](https://mermaid.live/view#pako:eNqNUstuwjAQ_JWVTyBBuefAIXDopSpSyK2Xrb0kqyZ2ajtUCPExVT-FH6tDDAqPSvXNnpmd0Y73QhpFIhGOPlvSkpaMhcX6TUM4KL2xkLsWLZv-qUHrWXKD2kNqzZcjew9kjWVdZCRby353j-dBlZHdsqR78MXowizTHoje0_k8miWguGCP0IaLxpqAwJEusedHVuBfZ0hg9ZqtYVaZgnVPvSYExSBVApVB1T2kuzwajc6O43O0Cz2IY-oENqzVQBTJEZ7e2uSX_Q3HPYjfwUvyyJX7I_6tIl3YXeOfavSyJDc6LWkCJbpy_M8J0jJCRs4dfww8r9erbtdYbREWxnwwPRwzrMqSYkvSw-w9CNx1RadVnNoN5Ry_Qy8YeWIiarI1shLJXviS6u6HKtpgW3lxOPwC9Kf0Ow)**

**Fluxo:** `POST /login` → Spring Security intercepta → `UserService.loadUserByUsername()` → MongoDB busca usuário → `BCrypt.matches()` → cria sessão HTTP com JSESSIONID → redirect `/books`.

---

### RF06 — Isolamento de Dados por Usuário

**Descrição:** Cada usuário só pode visualizar, editar e excluir os seus próprios livros.

**Testes:** `BookServiceIntegrationTest::shouldListOnlyOwnerBooks`, `shouldThrowExceptionWhenAccessingAnotherUserBook`  
**Tipo:** Integração (Testcontainers + MongoDB real)

**[📊 Ver Diagrama UML de Sequência](https://mermaid.live/view#pako:eNqVU0FuwjAQ_MrKJyqRco8EUlJQhdSWSqW3Xlx7CVYdL3XsUIR4DG_hY3VIgNJGKvjm3RnP7oy8ZoIkspgV-OnRCBwqnlmevxkIhwtHFl4Lz62ipK4tuHVKqAU3DlKijzsyzpLWaNv7L2hLJfBv85FMRsO0bhw0osHg_NEY7kdT6L2HYlFDz_sNoVGJYaaMTLROV5OlQdvxBdrkVsmbE7eBBmIzQU0CMhKBKtZYQh_2zJrV4KLfWg-qtFSApDD-EfxTomWb6j6cTtp3CfiDETE877aZMhwE5aDblJ7IhYFLPCUUwxSN406VHMIyXGBRcFuzqwL5oBQ287ttgF_hfE_JaP9KlPZQKndpEulqLBMjJ7WrHSW7cG0iSvb3yikc0-lfkI3xWrcpnIHc3NISxmH6jOvEZj4P9o2-BC6cIvN_RDZ4YVG4xiXWZTnanCvJ4jVzc8yrnyVxxr12bLP5BgbCMPk)**

**Mecanismo:** Todas as queries ao MongoDB incluem `ownerId` do usuário autenticado. Tentativa de acessar livro de outro usuário retorna `null` e dispara `IllegalArgumentException`.

---

### RF07 — Cadastro de Livro (CREATE)

**Descrição:** Usuário autenticado pode cadastrar um livro informando título, autor, ISBN e status de leitura.

**Testes:** `BookServiceIntegrationTest::shouldCreateBookSuccessfully`, `BookControllerE2ETest::shouldCreateBookAndRedirect`  
**Tipo:** Integração + E2E (MockMvc + Testcontainers)

**[📊 Ver Diagrama UML de Sequência](https://mermaid.live/view#pako:eNqFUstOwzAQ_JWVT0VKxT2Hij4ulUBFauHEZRtvWwvHLutNClT9GL6FH8OhliA0LTlEWu_M7HjWe1V4TSpXgV4qcgVNDK4ZyycH8cNCPMNDqJCNPx5tkcUUZotOYMR-F4g7Gt4_j70T9tae68-Ja1PQafPOu7WfjI6NNLs_GKRhOdwzRaMbgpXnsrKfH7EPmsCampPJhG1YLSeRPJsv4HoZTwMUvgQxYikDrGTjOQMTli5JtIgdSjePaI2GuvkjTBazC7x01xwKJhTqafEZ-J0jnuqrH16CRVLKIAd6NUHC6G0ajQ2dnh05vURKsP7fMSu0gbp0W6hAkvSAmmL8bU4P5aKjgDX1mgD_8dAUEWxrfyrXEWdTnwvx9_aZtGEqJC2xve8ITA8mh9sYHAJKFRf0jhpVpkriEo1W-V7Jhsrm2WtaYWVFHQ5fxv4N3w)**

---

### RF08 — ISBN Único por Usuário

**Descrição:** Não é permitido cadastrar dois livros com o mesmo ISBN para o mesmo usuário. ISBNs iguais entre usuários diferentes são permitidos.

**Testes:** `shouldThrowExceptionOnDuplicateIsbnSameOwner`, `shouldAllowSameIsbnForDifferentOwners`  
**Tipo:** Integração (Testcontainers + MongoDB real)

**[📊 Ver Diagrama UML de Sequência](https://mermaid.live/view#pako:eNqlkTFOw0AQRa8y2iqR7BpwEclWQHJBKNLSbLwTZ5XdHTM76xhFOQxn4WIYnCIRCAqm_fP-n685qoYMqkJFfEkYGlxa3bL2zwHG6TSLbWyng0BFtF8j97bB7-IjhZaW1SRcbOaLxVkqAAcbJVavddyEMpinQ0Cuzezu5jaDFJHL-YSfgXxkL5wKEE74U8D10o7pALVz2GpXcps8BrkfGuzEUoAc6nW1ApM6ZxttaPJbkSBQjwxXXg8uDQTaCXLQYnsacY_R05dJBpSEaTw9vb-xpX91r_7ovtUu4q8JUfc424zaXGXKI3ttjSqOSnboP_9rcKuTE3U6fQCBUKuj)**

---

### RF09 — Listagem de Livros (READ)

**Descrição:** Usuário autenticado pode visualizar todos os seus livros cadastrados.

**Teste:** `BookControllerE2ETest::shouldReturn200WithBookList`  
**Tipo:** E2E (MockMvc + Testcontainers)

**[📊 Ver Diagrama UML de Sequência](https://mermaid.live/view#pako:eNp1ksFKAzEQhl9lyEnBYvEYsNB1RcWWFVxvXmIyXYPZTE2yLVL6MD6LL2bCBu3aNYdA8n___JMhOyZJIePM43uHVmKpReNE-2whLiEDOXjynXCa-qu1cEFLvRY2QOFo69GNCERvV2SDI2P-0x_RbbTEY3FJtqGy6IWcPZnNchiHuUTvBZy_xCK-p7KWqEEyh5vrekgO9GzIrXBYaavmxhQf1daiO6G036nTX2smoy932XuArELIOFxC9zOVjE3-Ji20DwKiyeiNI3-cMPKWdC7ravwlhxO6mE6hugdJbSyfcm7r5WI4qYjn0XJ4-PpstBWZT-2wM9aia4VWjO9YeMU2fRGFK9GZwPb7b14zwt0)**

---

### RF10 — Busca por Título

**Descrição:** Usuário pode buscar livros pelo título de forma case-insensitive.

**Teste:** `BookServiceIntegrationTest::shouldSearchBooksIgnoringCase`  
**Tipo:** Integração (Testcontainers + MongoDB real)

**[📊 Ver Diagrama UML de Sequência](https://mermaid.live/view#pako:eNp1UttOAjEQ_ZVJnzSBSHzcRIyAEeKFBPHBxJfSHZaJux2cdpcQ4if5Ff6YXbZREOhbO-ecOWemG2U4RZUohx8lWoMD0pno4s1CONp4FnhxpRbi5mmpxZOhpbYeesIrh3KkwPzeZ-uF8_xU_RmlIoOHxUe2GQ96TSH2bne7sVkCKWXkNSy0yBqshlnpjG7QEVOj9xwkcHc7hYtZeHTXDrWYxdWWH2l74MiO_hJo8L31lHyOZ1taC3hlUUbp-Z9AxAd2DJDAnGwaebW8Jks2G2WWBfvandB6Yo_AFQr86mwTggmUNlmH1pGnCkEwbCcMYNjIDG8mk9cW7OSKAu3_iXKqhB2EddehdcruMMaRGdb3wXTsjg5td0OXnQ6M78FwETy6Mvc7LeKGAjyuNoGHxs6c8sYMLMOn899fvsxZtVSBUmhKVbJRfoFF_VlTnOsgqz4_fwCtI_cg)**

---

### RF11 — Filtro por Status de Leitura

**Descrição:** Usuário pode filtrar livros por status: `NÃO_LIDO`, `LENDO`, `LIDO`.

**Teste:** `BookServiceIntegrationTest::shouldFilterByEachReadingStatus` (parametrizado — testa os 3 status)  
**Tipo:** Integração (Testcontainers + MongoDB real)

**[📊 Ver Diagrama UML de Sequência](https://mermaid.live/view#pako:eNp1UkFOwzAQ_MrKJ5BaUXGMRBElCEUURaLlxmWxt8Ei8QbbaYWq_h2ncURTgk-2d2ZnZu29kKxIJMLRV0NGUqqxsFi9GQgLpWcLr65Bq7m7qtF6LXWNxsPC8s6RHSkwf96z8ZbL8r_6iuxWS_pbfGZTcLroClF7Op9HsQQclSQ1G4SNLoMELLM079AR06IHDhJ4fFjD1Xu4dLfOo2_czQlrgI3kaC8JIkYtvldH0p1R-c6QvWjJE-B2n6nL3zaRFXrEFD3_hVBpUwzbZGq0UaROz52UemvZgeQKugwwzNCLj8Rvz-k6d93IULEbjX465uvZDPKno1oUPufGYQdSfKUEljo464EIdfg9nVUxERXZCrUSyV74D6raT6dog03pxeHwAzqY4PY)**

---

### RF12 — Edição de Livro (UPDATE)

**Descrição:** Usuário pode editar informações de um livro existente. Não é permitido editar livros de outros usuários.

**Testes:** `BookServiceIntegrationTest::shouldUpdateBookSuccessfully`, `shouldThrowExceptionWhenUpdatingAnotherUserBook`  
**Tipo:** Integração (Testcontainers + MongoDB real)

**[📊 Ver Diagrama UML de Sequência](https://mermaid.live/view#pako:eNqNUk1vwjAM_StWTkwCcc8BiY4L0jYmMW67eLFh0dq4S1LQhvjvS2m0qhv7yC32e89-to_KCLHSKvBrw87wwuLOY_XoID00UTxsQoPeSheq0UdrbI0uQuHlENhfSIi8XIuLXsryp_ya_d4a_p68FbeTRdElconJbDbU1HC_Wj_A9ClFw_Ro6QRGKjBY1RIAY4OlfUeSkGUG5KyWO9DQ1ISRR5bGQFHGIAfHfklXPTdDEzF3p2FrHRVvS5o7WnX4s8CQm9GTrxXbD6Rxty2lLi8VGuD3yQ4hLNfFHfCnvWz3T3bguDlbpHkc_e4q4J5H7VD_Y6Af88Upt5xufRo8k_VsYt7YcLkJmG9Mw40NEXtlVGNVsa_QktJHFZ-5aq-VeItNGdXp9AF4HvP3)**

---

### RF13 — Exclusão de Livro (DELETE)

**Descrição:** Usuário pode remover um livro de sua biblioteca. Requer token CSRF. Não é permitido excluir livros de outros usuários.

**Testes:** `BookServiceIntegrationTest::shouldDeleteBookSuccessfully`, `BookControllerE2ETest::shouldDeleteBookAndRedirect`  
**Tipo:** Integração + E2E (MockMvc + Testcontainers)

**[📊 Ver Diagrama UML de Sequência](https://mermaid.live/view#pako:eNqFUs1OAjEQfpVJT5hAuPdAIhATEg1G9OaltgNM2Haw7S4awtP4KL6Y3ewkuoraWzvf38z0qCw7VFolfK4xWJyT2UTjHwOUY2zmCA-pNpG4e9qbmMnS3oQM08iHhPFMgXk345AjV9Vv9RXGhiz-LN5w2PB82hXEezSZiJkGy2FN0RvAF1vV6f1NkgmghfbsNdwuV_cwfiqvaXwkdxo7rDBjUfIwW91dQeYdBlHpcUVMsmroiANyQ-BDwLhwF580QRWOtKBhTcFNXxfuMrhlhx8IQSCj7w7tBcoi2gjG8Z_qkqZt7B9V3p3t7utYIzqKaLMMqj_TApRNaLimlA0k9MBQURO5UD035FgNlceyGXJKH1Xeom9_lsO1qausTqcPVODW6A)**

---

### RF14 — Proteção de Rotas (Autenticação Obrigatória)

**Descrição:** Todas as rotas protegidas devem redirecionar para `/login` quando acessadas sem sessão ativa.

**Teste:** `BookControllerE2ETest::shouldRedirectToLoginWhenNotAuthenticated`  
**Tipo:** E2E (MockMvc + Testcontainers)

**[📊 Ver Diagrama UML de Sequência](https://mermaid.live/view#pako:eNqNkcFKAzEQhl9lyFmp6C2HHkqLVESFSE-9jMl0Hdwk6yRbKaUPU3yUfTFTdikW92CO83_fZCbZKxsdKa0SfbYULM0ZK0G_DlAO2hwFVpw4Y8jUFxuUzJabUoGZxK9E8jcwjXCoDNlWOO_6_NznejodRF2uoJQQJm8xfiRI5KGOFYfeGKjCX_bTcL94HZyevMxHhC0Jb9giPJiFMcvnp-X8n6YpA3bfEThsu2PNDke93zvd3dyCkGMhm2Eysk-Bz4-h4aU7FgLBETyeWHWlPIlHdkrvVX4nf_ogRxts66wOhx-2IppQ)**

**Mecanismo:** Spring Security intercepta toda requisição, verifica `JSESSIONID`, e redireciona para `/login` se sessão inválida ou ausente.

---

### RF15 — Gerenciamento de Sessão (Login/Logout)

**Descrição:** O sistema deve manter sessão autenticada entre requisições e permitir logout seguro com invalidação da sessão.

**Teste:** `SecurityConfig` + `AuthControllerE2ETest::shouldShowLoginPage`  
**Tipo:** E2E (MockMvc + Testcontainers)

**[📊 Ver Diagrama UML de Sequência](https://mermaid.live/view#pako:eNqNkTFOAzEQRa8ycg2i3yIFBKQgRBCGjmZkD8soa88ytkEoymEQR8nFcFgXrEiBy_n_fft7tsaJJ9OZRK-FoqMlY68YniLUgy6LwmMqqCzTaETN7HjEmOFc5T2R_hXsqBx7S64o549Jbymni0XDOnADOwQKYJFbStOqa57Rwd3aPsDZIL2UDE4CXNj7K8iyoTihc-BIAsc3HNgjWEpp_yX_xHDEHuHaXlq7Wt-ulvVy2TAdpX-3U_Ks5PLPoznO-1Vj-5DabP9ZdQRPcHNwmhMTSAOyN93W5BcKhwV5esYyZLPbfQP0rplo)**

---

### RF16 — Validação de Formulários

**Descrição:** Formulários de cadastro e edição devem validar campos obrigatórios e exibir mensagens de erro adequadas.

**Teste:** `BookControllerE2ETest::shouldReturnFormWithErrorsWhenTitleEmpty`  
**Tipo:** E2E (MockMvc + Testcontainers)

**[📊 Ver Diagrama UML de Sequência](https://mermaid.live/view#pako:eNptkkFOAzEMRa9iZQ2iYjmLCg0VGyQGQWHFxp24bdQkLokDElUPwwE4RS9GZiYVjDTZ2f7P_8vKQbWsSVUq0nsi39LC4Cage_OQH7bCAV5iwmB4aO0xiGnNHr1AHfgzUpgYMO9u2UtgayfnhP4VrdEohv0wLy6X83lZW0FMK0dCsObgoGUHcvqRZBk-8Oucp4g7bGRawWPzvISrVe7GATZiaYSOgG7DKFYFN33R6xbLpkAjzRT0wFJb9LuzoWGLmifpidS18dr4zRPFZKXPTSFwnEz8_1bXsxk09z3Qn4tGXDlSBsqZK7jLqmRP37n4swFNUbDNeaO6UI6CQ6NVdVCyJdd9E01rzMHU8fgLQwTJdw)**

**Mecanismo:** Bean Validation (`@Valid`, `@NotBlank`) nas DTOs retorna erros via `BindingResult`. Controller renderiza o formulário novamente com as mensagens de erro.

---

## Estratégia de Testes

### Pirâmide de Testes Adotada

```
           /\
          /  \          ← E2E / Controller
         /────\            AuthControllerE2ETest, BookControllerE2ETest
        /      \           MockMvc + Testcontainers
       /────────\
      /          \      ← Integração
     /────────────\        UserServiceIntegrationTest, BookServiceIntegrationTest
    /              \       Testcontainers (MongoDB real)
   /────────────────\
  /                  \  ← Unitários
 /────────────────────\   BookDTOTest, UserRegistrationDTOTest
                          JUnit 5 puro (sem Spring, sem banco)
```

### Por que Testcontainers em vez de Mocks?

| Aspecto           | Mock                        | Testcontainers (MongoDB real)         |
|-------------------|-----------------------------|---------------------------------------|
| Fidelidade        | ⚠️ Simula comportamento     | ✅ Comportamento idêntico à produção  |
| Índices únicos    | ⚠️ Pode não respeitar       | ✅ Respeitados (como em produção)     |
| Queries complexas | ⚠️ Podem não funcionar      | ✅ Executam como em produção          |
| Velocidade        | ✅ Mais rápido               | ⚠️ Mais lento (inicia container)     |
| Confiabilidade    | ⚠️ Pode mascarar bugs       | ✅ Revela bugs reais                  |

**Conclusão:** Testcontainers garante que o que funciona nos testes, funciona em produção.

---

### Cobertura por Arquivo de Teste

| Arquivo de Teste                    | Tipo         | Requisitos Cobertos                            |
|-------------------------------------|--------------|------------------------------------------------|
| `UserRegistrationDTOTest`           | Unitário     | RF04                                           |
| `BookDTOTest`                       | Unitário     | RF16                                           |
| `UserServiceIntegrationTest`        | Integração   | RF01, RF02, RF03, RF04, RF05                   |
| `BookServiceIntegrationTest`        | Integração   | RF06, RF07, RF08, RF10, RF11, RF12, RF13       |
| `AuthControllerE2ETest`             | E2E          | RF15                                           |
| `BookControllerE2ETest`             | E2E          | RF06, RF07, RF09, RF13, RF14, RF16             |

---

*Disciplina: Qualidade de Software*  
*Autores: Alan Fonseca, Arielly Bispo, Eduardo Sampaio*