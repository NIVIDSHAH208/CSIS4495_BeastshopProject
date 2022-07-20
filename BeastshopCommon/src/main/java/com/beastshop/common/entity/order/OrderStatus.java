package com.beastshop.common.entity.order;

public enum OrderStatus {

	NEW {
		@Override
		public String defaultDescription() {
			return "Order was placed by the customer";
		}
	},
	CANCELLED {
		@Override
		public String defaultDescription() {
			return "Order was rejected";
		}
	},
	PROCESSING {
		@Override
		public String defaultDescription() {
			return "Order is under processing";
		}
	},
	PACKAGED {
		@Override
		public String defaultDescription() {
			return "Order is packed and ready for shipping";
		}
	},
	PICKED {
		@Override
		public String defaultDescription() {
			return "Order is on the move and picked by our courier partner";
		}
	},
	SHIPPING {
		@Override
		public String defaultDescription() {
			return "Order is being shipped to your location";
		}
	},
	DELIVERED {
		@Override
		public String defaultDescription() {
			return "Order was delivered";
		}
	},
	RETURNED {
		@Override
		public String defaultDescription() {
			return "Order is returend";
		}
	},
	PAID {
		@Override
		public String defaultDescription() {
			return "Payment received for the order";
		}
	},
	REFUNDED {
		@Override
		public String defaultDescription() {
			return "Order total has been refunded to customer";
		}
	},
	RETURN_REQUESTED {
		@Override
		public String defaultDescription() {
			return "Customer has requested to return the purchase";
		}
	};

	public abstract String defaultDescription();
}
