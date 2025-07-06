import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

import Slider from 'rc-slider';
import 'rc-slider/assets/index.css';

import React, { useEffect, useState } from 'react';

const TABS = ['All Orders', 'Completed', 'Continuing', 'Restitute', 'Canceled'];

export default function App() {
  const [orders, setOrders] = useState([]);
  const [activeTab, setActiveTab] = useState('All Orders');
  const [searchQuery, setSearchQuery] = useState('');
  const [page, setPage] = useState(0);

  // Filter modal
  const [showAdvancedFilters, setShowAdvancedFilters] = useState(false);
  const [startDate, setStartDate] = useState(null);
  const [endDate, setEndDate] = useState(null);
  const [minPrice, setMinPrice] = useState(0);
  const [maxPrice, setMaxPrice] = useState(999);

  // Fetch all orders
  useEffect(() => {
    fetch(`http://localhost:8080/api/orders?page=0&size=1000`)
      .then((res) => res.json())
      .then((data) => {
        setOrders(data.content);
      })
      .catch((err) => {
        console.error("Failed to fetch orders", err);
      });
  }, []);

  // Filter logic
  const filteredOrders = orders.filter(order => {
    const matchTab = activeTab === 'All Orders' || order.orderStatus === activeTab;

    const matchSearch =
      searchQuery.trim() === '' ||
      order.orderId.toLowerCase().includes(searchQuery.toLowerCase()) ||
      order.customerName.toLowerCase().includes(searchQuery.toLowerCase()) ||
      order.orderItem.toLowerCase().includes(searchQuery.toLowerCase()) ||
      order.orderStatus.toLowerCase().includes(searchQuery.toLowerCase());

    const matchDate =
      (!startDate || new Date(order.deliveryDate) >= startDate) &&
      (!endDate || new Date(order.deliveryDate) <= endDate);

    const matchPrice =
      (!minPrice || order.deliveryPrice >= minPrice) &&
      (!maxPrice || order.deliveryPrice <= maxPrice);

    return matchTab && matchSearch && matchDate && matchPrice;
  });

  const paginatedOrders = filteredOrders.slice(page * 10, (page + 1) * 10);
  const totalPages = Math.ceil(filteredOrders.length / 10);

  const resetFilters = () => {
    setStartDate(null);
    setEndDate(null);
    setMinPrice(0);
    setMaxPrice(20000);
  };

  return (
    <div className="container">
      <h1>Order Tracker</h1>

      {/* Tabs */}
      <div className="tabs">
        {TABS.map(tab => (
          <button
            key={tab}
            className={activeTab === tab ? 'tab active' : 'tab'}
            onClick={() => {
              setActiveTab(tab);
              setPage(0);
            }}
          >
            {tab}
          </button>
        ))}
      </div>

      {/* Search & Filter */}
      <div className="search-filter-bar">
        <input
          type="text"
          placeholder="Search for order ID, customer, item..."
          value={searchQuery}
          onChange={(e) => {
            setSearchQuery(e.target.value);
            setPage(0);
          }}
        />

        <button
          className="filter-btn"
          onClick={() => setShowAdvancedFilters(true)}
        >
          Filters
        </button>
      </div>

      {/* Filter Modal */}
      {showAdvancedFilters && (
        <div className="modal-overlay">
          <div className="filter-modal">
            <h3>Advanced Filters</h3>

            {/* Delivery Date */}
            <div className="filter-group">
              <label>Delivery Date Range</label>
              <div className="filter-inline">
                <DatePicker
                  selected={startDate}
                  onChange={(date) => setStartDate(date)}
                  selectsStart
                  startDate={startDate}
                  endDate={endDate}
                  placeholderText="Start Date"
                  className="datepicker"
                />
                <DatePicker
                  selected={endDate}
                  onChange={(date) => setEndDate(date)}
                  selectsEnd
                  startDate={startDate}
                  endDate={endDate}
                  minDate={startDate}
                  placeholderText="End Date"
                  className="datepicker"
                />
              </div>
            </div>

            {/* Delivery Price */}
            <div className="filter-group">
              <label>Delivery Price Range</label>
              <div className="filter-inline slider-block">
                <Slider
                  range
                  min={0}
                  max={999}
                  value={[minPrice, maxPrice]}  // ⬅️ controlled value
                  onChange={([min, max]) => {
                    setMinPrice(min);
                    setMaxPrice(max);
                  }}
                  allowCross={false}
                  trackStyle={[{ backgroundColor: '#1976d2' }]}
                  handleStyle={[
                    { borderColor: '#1976d2' },
                    { borderColor: '#1976d2' }
                  ]}
                />
                <div className="price-values">₹{minPrice} – ₹{maxPrice}</div>
              </div>
            </div>

            {/* Actions */}
            <div className="filter-actions">
              <button onClick={resetFilters} className="reset-btn">Reset Filters</button>
              <button onClick={() => setShowAdvancedFilters(false)}>Close</button>
            </div>
          </div>
        </div>
      )}

      {/* Table */}
      <div className="order-table">
        <div className="order-headers">
          <div>Order ID</div>
          <div>Customer</div>
          <div>Item</div>
          <div>Delivery Date</div>
          <div>Delivery Price</div>
          <div>Status</div>
        </div>

        {paginatedOrders.length === 0 ? (
          <p>No orders found.</p>
        ) : (
          paginatedOrders.map(order => (
            <div key={order.id} className="order-row">
              <div>{order.orderId}</div>
              <div className="semi-bold">{order.customerName}</div>
              <div>{order.orderItem}</div>
              <div>{order.deliveryDate}</div>
              <div className="semi-bold">₹{order.deliveryPrice}</div>
              <div>
                <span className={`badge ${order.orderStatus.toLowerCase()}`}>
                  {order.orderStatus}
                </span>
              </div>
            </div>
          ))
        )}
      </div>

      {/* Pagination */}
      <div className="pagination">
        <div className="page-info">
          Showing {filteredOrders.length === 0 ? 0 : page * 10 + 1}–
          {Math.min((page + 1) * 10, filteredOrders.length)} of {filteredOrders.length} entries
        </div>

        <div className="page-controls">
          <button onClick={() => setPage(page - 1)} disabled={page === 0}>⬅️ Prev</button>

          <input
            type="number"
            min="1"
            max={totalPages}
            value={totalPages === 0 ? 1 : page + 1}
            onChange={(e) => {
              const newPage = Number(e.target.value) - 1;
              if (newPage >= 0 && newPage < totalPages) setPage(newPage);
            }}
            className="page-input"
          />

          <span>of {totalPages}</span>

          <button onClick={() => setPage(page + 1)} disabled={page + 1 >= totalPages}>Next ➡️</button>
        </div>
      </div>

    </div>
  );
}
