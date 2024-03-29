package com.naever.store.domain.review.service

import com.naever.store.domain.exception.ForbiddenException
import com.naever.store.domain.exception.ModelNotFoundException
import com.naever.store.domain.order.repository.OrderItemRepository
import com.naever.store.domain.review.dto.CreateReviewRequest
import com.naever.store.domain.review.dto.ReviewResponse
import com.naever.store.domain.review.dto.UpdateReviewRequest
import com.naever.store.domain.review.model.Review
import com.naever.store.domain.review.model.toResponse
import com.naever.store.domain.review.repositiory.ReviewRepository
import com.naever.store.domain.user.repository.UserRepository
import com.naever.store.infra.security.SecurityUtil
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReviewServiceImpl(
    private val reviewRepository: ReviewRepository,
    private val orderItemRepository: OrderItemRepository,
    private val userRepository: UserRepository
) : ReviewService {

    //리뷰를 조회
    override fun getReview(orderItemId: Long): ReviewResponse{
        val orderItem = orderItemRepository.findByIdOrNull(orderItemId)
            ?: throw ModelNotFoundException("OrderItem", orderItemId)
        val review = reviewRepository.findByOrderItemId(orderItemId) ?: throw ModelNotFoundException("Review",null)
        return review.toResponse()
    }

    @Transactional
    override fun createReview(orderItemId: Long, request: CreateReviewRequest): ReviewResponse{

        val userId = SecurityUtil.getLoginUserId()
        val user = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("User", userId)

        val orderItem = orderItemRepository.findByIdOrNull(orderItemId) ?: throw ModelNotFoundException("orderItem", orderItemId)

        if (orderItem.orderStore.order.user.id != userId) {
            throw ForbiddenException(userId!!, "OrderItem", orderItemId)
        }

        if (reviewRepository.existsByOrderItemId(orderItemId)) {
            throw IllegalStateException("already written a review")
        }

        return reviewRepository.save(
                Review(
                    rating = request.rating,
                    content = request.content,
                    orderItem = orderItem,
                    user = user
                )
        ).toResponse()
    }

    @Transactional
    override fun updateReview(orderItemId: Long, reviewId: Long, request: UpdateReviewRequest): ReviewResponse{

        val orderItem = orderItemRepository.findByIdOrNull(orderItemId)
            ?: throw ModelNotFoundException("OrderItem", orderItemId)

        val userId = SecurityUtil.getLoginUserId()

        if (orderItem.orderStore.order.user.id != userId) {
            throw ForbiddenException(userId!!, "OrderItem", orderItemId)
        }

        val review = reviewRepository.findByOrderItemIdAndId(orderItemId,reviewId)
            ?: throw ModelNotFoundException("Review", reviewId)

        if (review.user.id != userId) {
            throw ForbiddenException(userId!!, "Review", reviewId)
        }

        review.rating = request.rating
        review.content = request.content
        return reviewRepository.save(review).toResponse()
    }

    @Transactional
    override fun deleteReview(orderItemId: Long, reviewId: Long){
        val orderItem = orderItemRepository.findByIdOrNull(orderItemId)
            ?: throw ModelNotFoundException("OrderItem",orderItemId)
        val review = reviewRepository.findByIdOrNull(reviewId) ?: throw ModelNotFoundException("Review",reviewId)

        val userId = SecurityUtil.getLoginUserId()

        if (review.user.id != userId) {
            throw ForbiddenException(userId!!, "Review", reviewId)
        }

        review.deleteReview()
        reviewRepository.save(review)
    }
}