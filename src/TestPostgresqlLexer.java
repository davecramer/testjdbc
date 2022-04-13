import org.postgresql.util.BasicPostgresqlSqlLexer;
import org.postgresql.util.Token;
import org.postgresql.util.TokenizedSql;
import org.postgresql.util.TokenizedStatement;

import java.util.List;
/*
"SELECT (api_asset_traits.asset_id) AS _prefetch_related_val_asset_id, api_trait.id, api_trait.created_date, api_trait.modified_date, api_trait.asset_contract_id, api_trait.collection_id, api_trait.trait_type, api_trait.value, api_trait.int_value, api_trait.float_value, api_trait.date_value, api_trait.trait_count, api_trait.display_type, api_trait.max_value, api_trait.order FROM api_trait INNER JOIN api_asset_traits ON (api_trait.id = api_asset_traits.trait_id) WHERE api_asset_traits.asset_id IN (1444739, 1444740, 1444741, 2919031, 17107788, 17304488, 18123572, 21367388, 21367695, 22612567, 25660171, 26774240, 33360348, 33360407, 34999177, 35588964, 42207724, 42207796, 52103767, 55609754, 55610011, 57346931, 62425603, 62425623, 62425647, 62425658, 62425840, 62425868, 62425929, 62425933, 62425938, 62425969, 62425989, 62426000, 62426026, 62426046, 62458251,  62458317, 62458331, 62458378, 62458391, 62458408, 62458423, 62458618, 62458632 )"
 */
public class TestPostgresqlLexer {
    public static void main(String[] args) {
        TokenizedSql tokenizedSql = BasicPostgresqlSqlLexer.tokenize("select ?");
        List<TokenizedStatement> statements = tokenizedSql.getStatements();
        for (TokenizedStatement ts : statements ) {
            List <Token> tokens = ts.getTokens();
            for (Token token : tokens ) {
                System.out.println(token.toString());
            }
        }
    }
}
