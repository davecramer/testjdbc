package org.postgresql.util;

/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.util.List;

public class TokenizedSql {

    private final String sql;

    private final List<TokenizedStatement> statements;

    private final int statementCount;

    private final int parameterCount;

    public TokenizedSql(String sql, List<TokenizedStatement> statements) {
        this.sql = sql;
        this.statements = statements;
        this.statementCount = statements.size();
        this.parameterCount = getParameterCount(statements);
    }

    public List<TokenizedStatement> getStatements() {
        return statements;
    }

    public int getStatementCount() {
        return this.statementCount;
    }

    public int getParameterCount() {
        return this.parameterCount;
    }

    public String getSql() {
        return sql;
    }

    private static int getParameterCount(List<TokenizedStatement> statements) {
        int sum = 0;
        for (TokenizedStatement statement : statements){
            sum += statement.getParameterCount();
        }
        return sum;
    }

    public boolean hasDefaultTokenValue(String... tokenValues) {
        for (TokenizedStatement statement : statements) {
            for (Token token : statement.getTokens()) {
                if (token.getType() == TokenType.DEFAULT) {
                    for (String value : tokenValues) {
                        if (token.getValue().equalsIgnoreCase(value)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

}