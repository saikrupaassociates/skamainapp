select a.ORDER_CODE, b.DELIVERED_QUANTITY, a.PRICE,a.DELIVERY_COST,a.DISCOUNT, b.DELIVERY_DATE from com_order_entry a, com_order_delivery b
where a.ORDER_CODE=b.ORDER_CODE
and a.ENTRY_NO=b.ENTRY_NO;


select sum(QUANTITY_ADDED) from inventory_entry where CREATED_DATE between '2019-01-01 00:00:00' and '2019-01-31 23:59:59';

select sum(ceil((e.QUANTITY * e.PRICE) + e.DELIVERY_COST - e.DISCOUNT)) as 'Order Value' from com_order_entry e, com_order_delivery c
where e.order_code=c.order_code
and c.DELIVERY_DATE between '2019-01-01 00:00:00' and '2019-01-31 23:59:59';





