package com.example.currencies.conversion.navigation

import androidx.navigation.NavController
import com.example.currencies.R
import com.example.currencies.common.entities.Amount
import com.example.currencies.common.entities.Currency
import com.example.currencies.compare.view.CurrencyCompareFragment
import com.example.currencies.conversion.CurrencyConversionNavigator
import javax.inject.Inject

class CurrencyConversionNavigatorImpl @Inject constructor(
    private val navController: dagger.Lazy<NavController>
): CurrencyConversionNavigator {

    override fun goToCurrencyCompare(amount: Amount, firstCurrency: Currency, secondCurrency: Currency) {
        navController.get().navigate(
            R.id.to_currency_compare,
            // todo: avoid referencing the fragment class from outside the "compare" package
            // expose the method to create the args bundle without exposing the fragment
            CurrencyCompareFragment.createArgsBundle(amount, firstCurrency, secondCurrency)
        )
    }

}